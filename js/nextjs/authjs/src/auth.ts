import NextAuth from "next-auth";
import { DrizzleAdapter } from "@auth/drizzle-adapter";
import { db } from "@/db";
import Credentials from "next-auth/providers/credentials";
import { LoginSchema } from "@/schemas";
import { getUserByEmail, getUserById } from "@/data/users";
import bcrypt from "bcryptjs";
import Google from "next-auth/providers/google";
import { twoFaConfirmation, users } from "@/db/schemas/user";
import { eq } from "drizzle-orm";
import { getTwoFactorConfirmationByUserId } from "@/data/two-factor-confirmation";

/*
Sources:
  https://authjs.dev/getting-started
  https://github.com/imakhlaq/ZeoCoupon/blob/main/src/auth.ts
  https://www.youtube.com/watch?v=1MTyCvS05V4&list=PL48dtV23n4TU_4WDl8oWr2_WFwyKaW5NN
  
 */

export const { handlers, signIn, signOut, auth } = NextAuth({
  adapter: DrizzleAdapter(db),
  session: {
    strategy: "jwt", //using jwt ( you can also use session strategy)
    maxAge: 30 * 24 * 60 * 60, //max signed in allowed
  },
  providers: [
    Google({
      clientId: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    }),
    Credentials({
      name: "credentials",
      authorize: async (credentials) => {
        const validatedFields = LoginSchema.safeParse(credentials);
        if (!validatedFields.success) return null;

        const { email, password } = validatedFields.data;
        const user = await getUserByEmail(email);

        if (!user || !user.password) return null;

        const passwordMatch = await bcrypt.compare(password, user.password);
        if (passwordMatch) return user;
        return null;
      },
    }),
  ],
  pages: {
    signIn: "/auth/login",
    error: "/auth/error",
  },

  //events are fired when the something happens user logs in or updates the account or add a social app to login
  events: {
    //its called when a user sign up with the social login
    async linkAccount({ user }) {
      if (!user.id) return;

      //setting email verified for the users signed with socials
      await db
        .update(users)
        .set({ email: new Date().toDateString() })
        .where(eq(users.id, user.id));
    },
  },
  callbacks: {
    async jwt({ token, user }) {
      //if token doesn't have a sub(pk id) then return the token
      if (!token.sub) return token;

      const existingUser = await getUserById(token.sub);
      if (!existingUser) return token;

      //adding custom field on the token
      token.role = existingUser.role;
      token.twoFactorAuthStatus = existingUser.is2FaEnable ?? false;

      return token;
    },
    async session({ token, session }) {
      //adding user id to the session
      if (token.sub && session.user) {
        session.user.id = token.sub;
      }

      if (token.role && session.user) {
        session.user.role = token.role;
        session.user.twoFactorAuthStatus = token.twoFactorAuthStatus;
      }
      return session;
    },

    //called when login (even when user uses auth provider or credentials)
    async signIn({ user, account }) {
      //allow oauth users to login without email verification
      if (account?.provider !== "credentials") return true;

      if (!user.id) return false;
      const existingUser = await getUserById(user.id);
      // if user is using "credentials" and email is not verified
      if (!existingUser?.emailVerified) return false;

      //checking for 2-factor auth
      if (existingUser.is2FaEnable) {
        /*
                                                                                twoFaConfirmation represent the success authentication,
                                                                                After sending a 2FA token user confirms it, and it will create a twoFaConfirmation,
                                                                                        
                                                                                         */
        const twoFactorConfirmation = await getTwoFactorConfirmationByUserId(
          existingUser.id,
        );
        //if we don't have 2Fa Confirmation for a user then don't allow him to login
        if (!twoFactorConfirmation) return false;

        //and if we have a 2FA confirmation then delete(because next time user signs in they need to perfom)
        // it and allow user to login
        await db
          .delete(twoFaConfirmation)
          .where(eq(twoFaConfirmation.userId, user.id));
      }

      return true; //returning true mean user is allowed to log in
    },
  },
});