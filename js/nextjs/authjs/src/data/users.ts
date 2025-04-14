import { db } from "@/db";
import { users } from "@/db/schemas/user";
import { eq } from "drizzle-orm";

export async function getUserByEmail(email: string) {
  return db.selectDistinct().from(users).where(eq(users.email, email));
}