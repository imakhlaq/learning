import NextAuth from "next-auth";
import { DrizzleAdapter } from "@auth/drizzle-adapter";
import { db } from "@/db";
import Credentials from "next-auth/providers/credentials";
import { LoginSchema } from "@/schemas";
import { getUserByEmail } from "@/data/users";
import bcrypt from "bcryptjs";

export const { handlers, signIn, signOut, auth } = NextAuth({
  adapter: DrizzleAdapter(db),
  session: { strategy: "jwt" }, //using jwt ( you can also use session strategy)
  providers: [
    Credentials({
      name: "credentials",
      authorize: async (credentials) => {
        const validatedFields = LoginSchema.safeParse(credentials);
        if (!validatedFields.success) return null;

        const { email, password } = validatedFields.data;
        const user = await getUserByEmail(email);

        if (!user[0] || !user[0].password) return null;

        const passwordMatch = await bcrypt.compare(password, user[0].password);
        if (passwordMatch) return user[0];
        return null;
      },
    }),
  ],
});