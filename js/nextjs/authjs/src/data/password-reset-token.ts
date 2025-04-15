import { db } from "@/db";
import { passwordResetTokens } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function passwordResetTokenByToken(token: string) {
  try {
    const passwordToken = await db
      .select()
      .from(passwordResetTokens)
      .where(eq(passwordResetTokens.token, token));

    if (passwordToken.length == 0) return null;
    return passwordToken[0];
  } catch (e) {
    return null;
  }
}

export async function passwordResetTokenByEmail(email: string) {
  try {
    const passwordToken = await db
      .select()
      .from(passwordResetTokens)
      .where(eq(passwordResetTokens.email, email));

    if (passwordToken.length == 0) return null;
    return passwordToken[0];
  } catch (e) {
    return null;
  }
}