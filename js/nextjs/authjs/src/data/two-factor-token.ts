import { db } from "@/db";
import { twoFactorAuthenticationTokens } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function getTwoFactorTokenByToken(token: string) {
  try {
    const twoFactorToken = await db
      .select()
      .from(twoFactorAuthenticationTokens)
      .where(eq(twoFactorAuthenticationTokens.token, token));

    if (twoFactorToken.length == 0) return null;

    return twoFactorToken[0];
  } catch (e) {
    return null;
  }
}

export async function getTwoFactorTokenByEmail(email: string) {
  try {
    const twoFactorToken = await db
      .select()
      .from(twoFactorAuthenticationTokens)
      .where(eq(twoFactorAuthenticationTokens.email, email));

    if (twoFactorToken.length == 0) return null;

    return twoFactorToken[0];
  } catch (e) {
    return null;
  }
}