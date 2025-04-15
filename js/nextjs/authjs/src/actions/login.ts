/*
This is a server action its same as writing an API and passing data on body
 */

"use server";
import * as z from "zod";
import { LoginSchema } from "../schemas";
import { signIn } from "@/auth";
import { DEFAULT_LOGIN_REDIRECT } from "@/routes";
import { AuthError } from "next-auth";
import { getUserByEmail } from "@/data/users";
import { generateVerificationToken } from "@/lib/tokens";
import { sendEmailVerification, sendTwoFactorEmail } from "@/lib/mail";
import { getTwoFactorTokenByEmail } from "@/data/two-factor-token";
import { db } from "@/db";
import {
  twoFaConfirmation,
  twoFactorAuthenticationTokens,
} from "@/db/schemas/user";
import { eq } from "drizzle-orm";
import { getTwoFactorConfirmationByUserId } from "@/data/two-factor-confirmation";

export async function login(vales: z.infer<typeof LoginSchema>) {
  const validatedFiled = LoginSchema.safeParse(vales);
  if (!validatedFiled.success) return { error: "Invalid fields!" };

  const { email, password, code } = validatedFiled.data;

  //check is use exists
  const existingUser = await getUserByEmail(email);
  //user exits, and it does have email, and it does have password(means he needs to sign in throw socials)
  if (!existingUser || !existingUser.email || !existingUser.password) {
    return { error: "Invalid credentials" };
  }

  //if user email is not verified
  if (!existingUser.emailVerified) {
    const verificationEmail = await generateVerificationToken(email);
    await sendEmailVerification(
      verificationEmail?.email!,
      verificationEmail?.token!,
    );

    return { error: "Confirm the email!" };
  }

  //check is 2FA is enabled then send token
  if (existingUser?.is2FaEnable && existingUser?.email) {
    //check if use have sent the code
    if (code) {
      //user have also sent the code with email and password
      const twoFactorToken = await getTwoFactorTokenByEmail(existingUser.email);

      if (!twoFactorToken) return { error: "Invalid code!" };
      if (twoFactorToken.token !== code) return { error: "Invalid code!" };

      const hasExpired = new Date(twoFactorToken.expires!) < new Date();

      if (hasExpired) return { error: "Code expired!" };

      //delete the token
      await db
        .delete(twoFactorAuthenticationTokens)
        .where(eq(twoFactorAuthenticationTokens.email, existingUser.email));

      //get if there is previous confirmation
      const existingConfirmation = await getTwoFactorConfirmationByUserId(
        existingUser.id,
      );

      //delete the existing confirmation
      if (existingConfirmation) {
        await db
          .delete(twoFactorAuthenticationTokens)
          .where(eq(twoFactorAuthenticationTokens.id, existingConfirmation.id));
      }

      //now create and save an authentication confirmation (to represent user is now uthenticated
      //and in await signIn("credential"{data}) the
      await db.insert(twoFaConfirmation).values({ userId: existingUser.id });
    } else {
      // if frontend did not send code then
      //we generate a token
      const twoFactorToken = await generateVerificationToken(
        existingUser.email!,
      );
      //sending 2-factor code
      await sendTwoFactorEmail(twoFactorToken?.email!, twoFactorToken?.token!);
      return { twoFactor: true };
    }
  }

  try {
    //login using server actions
    await signIn("credentials", {
      email,
      password,
      redirectTo: DEFAULT_LOGIN_REDIRECT,
    });
  } catch (e) {
    if (e instanceof AuthError) {
      switch (e.type) {
        case "CredentialsSignin":
          return { error: "Invalid credentials!" };
        default:
          return { error: "Something went wrong!" };
      }
    }
    throw e;
  }

  return { success: "Email sent!" };
}