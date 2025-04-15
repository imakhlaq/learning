/*
This is a server action its same as writing an API and passing data on body
 */

"use server";
import * as z from "zod";
import { RegisterSchema } from "@/schemas";
import bcrypt from "bcryptjs";
import { db } from "@/db";
import { users } from "@/db/schemas/user";
import { getUserByEmail } from "@/data/users";
import { generateVerificationToken } from "@/lib/tokens";
import { sendEmailVerification } from "@/lib/mail";

export async function register(vales: z.infer<typeof RegisterSchema>) {
  const validatedFiled = RegisterSchema.safeParse(vales);
  if (!validatedFiled.success) return { error: "Invalid fields!" };

  const { name, password, email } = validatedFiled.data;

  //check if email is not taken
  const existingUser = await getUserByEmail(email);
  if (existingUser) return { error: "Email is already registered" };

  const hashedPassword = await bcrypt.hash(password, 10);
  await db
    .insert(users)
    .values({ name: name, email: email, password: hashedPassword });

  //generate the token
  const verificationToken = await generateVerificationToken(email);
  await sendEmailVerification(
    verificationToken?.email!,
    verificationToken?.token!,
  );

  return { success: "Confirmation Email sent!" };
}