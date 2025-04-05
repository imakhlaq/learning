//this is catch all routes

import app from "@/server";
import { handle } from "hono/vercel";

export const runtime = "edge"; //by this it will be compatible with cloudeflare and vercel

export const OPTIONS = handle(app);
export const GET = handle(app);
export const POST = handle(app);
export const PUT = handle(app);
export const PATCH = handle(app);
export const DELETE = handle(app);

export default app as never;