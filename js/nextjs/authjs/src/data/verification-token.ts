import { db } from "@/db";
import { verificationTokens } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function getVerificationEmail(email: string) {
  const verificationToken = await db
    .select()
    .from(verificationTokens)
    .where(eq(verificationTokens.email, email));

  if (verificationToken.length == 0) return null;
  return verificationToken[0];
}

export async function getVerificationToken(token: string) {
  const verificationToken = await db
    .select()
    .from(verificationTokens)
    .where(eq(verificationTokens.token, token));

  if (verificationToken.length == 0) return null;
  return verificationToken[0];
}