import { db } from "@/db";
import { twoFaConfirmation } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function getTwoFactorConfirmationByUserId(userId: string) {
  try {
    const twoFactorConfirmation = await db
      .select()
      .from(twoFaConfirmation)
      .where(eq(twoFaConfirmation.userId, userId));

    if (twoFactorConfirmation.length == 0) return null;
    return twoFactorConfirmation[0];
  } catch (e) {
    return null;
  }
}