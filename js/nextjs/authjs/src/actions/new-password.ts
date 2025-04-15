/*
This is a server action its same as writing an API and passing data on body
 */

"use server";
import { z } from "zod";
import { NewPasswordSchema } from "@/schemas";
import { generatePasswordResetToken } from "@/lib/tokens";
import { getUserByEmail } from "@/data/users";
import bcrypt from "bcryptjs";
import { db } from "@/db";
import { users } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function newPassword(
  values: z.infer<typeof NewPasswordSchema>,
  token: string | null,
) {
  if (!token) return { error: "Missing token" };

  let validated = NewPasswordSchema.safeParse(values);
  if (!validated.success)
    return { error: "Please enter with atleast 6 character" };

  const { password } = validated.data;

  const existingTokens = await generatePasswordResetToken(token);

  if (!existingTokens) return { error: "Invalid token" };

  const hasTokenExpired = new Date(existingTokens.expires!) < new Date();
  if (hasTokenExpired) return { error: "Toke has expired" };

  const existingUser = await getUserByEmail(existingTokens.email!);
  if (!existingUser) return { error: "User doesn't exit" };

  const hashedPassword = await bcrypt.hash(validated.data.password, 10);

  await db
    .update(users)
    .set({ password: hashedPassword })
    .where(eq(users.id, existingUser.id));

  return { success: "Password Reset successful" };
}