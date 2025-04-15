"use server";
import * as z from "zod";
import { LoginSchema } from "../schemas";
import { signIn } from "@/auth";
import { DEFAULT_LOGIN_REDIRECT } from "@/routes";
import { AuthError } from "next-auth";
import { getUserByEmail } from "@/data/users";
import { generateVerificationToken } from "@/lib/tokens";
import { sendEmailVerification } from "@/lib/mail";

export async function login(vales: z.infer<typeof LoginSchema>) {
  const validatedFiled = LoginSchema.safeParse(vales);
  if (!validatedFiled.success) return { error: "Invalid fields!" };

  const { email, password } = validatedFiled.data;

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