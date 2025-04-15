import { db } from "@/db";
import { users } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function getUserByEmail(email: string) {
  const user = await db.select().from(users).where(eq(users.email, email));

  if (user.length == 0) return null;
  return user[0];
}

export async function getUserById(id: string) {
  const user = await db.select().from(users).where(eq(users.id, id));

  if (user.length == 0) return null;
  return user[0];
}