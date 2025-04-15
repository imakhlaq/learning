import { v4 as uuidv4 } from "uuid";
import crypto from "crypto";
import { getVerificationEmail } from "@/data/verification-token";
import { db } from "@/db";
import {
  passwordResetTokens,
  twoFactorAuthenticationTokens,
  verificationTokens,
} from "@/db/schemas/user";
import { eq } from "drizzle-orm";
import { passwordResetTokenByEmail } from "@/data/password-reset-token";
import { getTwoFactorConfirmationByUserId } from "@/data/two-factor-confirmation";
import { getTwoFactorTokenByEmail } from "@/data/two-factor-token";

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

export async function generateTwoFactorToken(email: string) {
  const token = crypto.randomInt(100_000, 1_000_000).toString();
  const expires = new Date(new Date().getTime() + 3600 * 1000).toDateString();

  const existingToken = await getTwoFactorTokenByEmail(email);
  if (existingToken) {
    await db
      .delete(twoFactorAuthenticationTokens)
      .where(eq(twoFactorAuthenticationTokens.id, existingToken.id));
  }

  const twoFactorToken = await db.insert(twoFactorAuthenticationTokens).values({
    email,
    token,
    expires,
  });

  return twoFactorToken;
}