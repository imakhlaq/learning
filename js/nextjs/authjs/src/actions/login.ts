"use server";
import * as z from "zod";
import { LoginSchema } from "../schemas";
import { signIn } from "@/auth";
import { DEFAULT_LOGIN_REDIRECT } from "@/routes";
import { AuthError } from "next-auth";

export async function login(vales: z.infer<typeof LoginSchema>) {
  const validatedFiled = LoginSchema.safeParse(vales);
  if (!validatedFiled.success) return { error: "Invalid fields!" };

  const { email, password } = validatedFiled.data;

  try {
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