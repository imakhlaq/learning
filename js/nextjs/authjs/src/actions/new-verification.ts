/*
This is a server action its same as writing an API and passing data on body
 */

"use server";
import { getVerificationToken } from "@/data/verification-token";
import { getUserByEmail } from "@/data/users";
import { db } from "@/db";
import { users, verificationTokens } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function newVerification(token: string) {
  const existingToken = await getVerificationToken(token);
  if (!existingToken) return { error: "Token does not exits!" };

  const hasExpired = new Date(existingToken.expires!) < new Date();
  if (hasExpired) return { error: "Token has expired!" };

  const existingUser = await getUserByEmail(existingToken.email!);
  if (!existingUser) return { error: "Email does not exits!" };

  await db
    .update(users)
    .set({ emailVerified: new Date(), email: existingToken.email })
    .where(eq(users.id, existingUser.id));

  await db
    .delete(verificationTokens)
    .where(eq(verificationTokens.id, existingToken.id));

  return { success: "Email verified" };
}