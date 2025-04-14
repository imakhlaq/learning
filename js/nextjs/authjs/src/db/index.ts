import "dotenv/config";
import { drizzle, NodePgDatabase } from "drizzle-orm/node-postgres";

/*
Because of Next.js hot reload every time new instance of postgress will be created.
this solves this issue
 */

declare global {
  var drizzle: undefined | NodePgDatabase;
}

export const db = globalThis.drizzle || drizzle(process.env.DATABASE_URL!);

if (process.env.NODE_ENV !== "production") globalThis.drizzle = db;