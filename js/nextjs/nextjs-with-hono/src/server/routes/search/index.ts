import { OpenAPIHono } from "@hono/zod-openapi";

import type { ContextVariables } from "@/server/types";
import { search } from "@/server/routes/search/search";

export const searchApp = new OpenAPIHono<{
  Variables: ContextVariables;
}>()
  .route("/", search)
  .route("/");