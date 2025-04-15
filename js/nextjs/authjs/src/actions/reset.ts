"use server";
import * as z from "zod";
import { ResetSchema } from "@/schemas";
import { getUserByEmail } from "@/data/users";
import { generatePasswordResetToken } from "@/lib/tokens";
import { sendPasswordResetEmail } from "@/lib/mail";

export async function restPassword(values: z.infer<typeof ResetSchema>) {
  const validateFields = ResetSchema.safeParse(values);
  if (!validateFields.success) return { error: "Invalid email" };

  const existingUser = await getUserByEmail(values.email);
  if (!existingUser) return { error: "Email not register with us" };

  const passwordResetToken = await generatePasswordResetToken(
    existingUser.email!,
  );
  await sendPasswordResetEmail(
    passwordResetToken?.email!,
    passwordResetToken?.token!,
  );

  return { success: "Reset email sent" };
}