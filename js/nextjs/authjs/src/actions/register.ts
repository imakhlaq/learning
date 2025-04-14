import * as z from "zod";
import { RegisterSchema } from "@/schemas";
import bcrypt from "bcryptjs";
import { db } from "@/db";
import { users } from "@/db/schemas/user";
import { getUserByEmail } from "@/data/users";

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

  return { success: "Email sent!" };
}