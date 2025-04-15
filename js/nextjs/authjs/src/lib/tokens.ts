import { v4 as uuidv4 } from "uuid";
import { getVerificationEmail } from "@/data/verification-token";
import { db } from "@/db";
import { passwordResetTokens, verificationTokens } from "@/db/schemas/user";
import { eq } from "drizzle-orm";
import { passwordResetTokenByEmail } from "@/data/password-reset-token";

export async function generateVerificationToken(email: string) {
  const token = uuidv4();
  const expires = new Date(new Date().getTime() + 3600 * 1000).toDateString();

  //if we have sent a token already
  const existingToken = await getVerificationEmail(email);
  if (existingToken) {
    //remove the verification from db
    await db
      .delete(verificationTokens)
      .where(eq(verificationTokens.id, existingToken.id));
  }
  //generate new verification table
  const verificationToken = await db
    .insert(verificationTokens)
    .values({
      email,
      token,
      expires,
    })
    .returning();

  if (verificationToken.length == 0) return null;

  return verificationToken[0];
}

export async function generatePasswordResetToken(email: string) {
  const token = uuidv4();
  const expires = new Date(new Date().getTime() + 3600 * 1000).toDateString();

  //if we have sent a token already
  const existingToken = await passwordResetTokenByEmail(email);
  if (existingToken) {
    //remove the verification from db
    await db
      .delete(passwordResetTokens)
      .where(eq(passwordResetTokens.id, existingToken.id));
  }

  //generate new verification table
  const passwordResetToken = await db
    .insert(passwordResetTokens)
    .values({
      email,
      token,
      expires,
    })
    .returning();

  if (passwordResetToken.length == 0) return null;

  return passwordResetToken[0];
}