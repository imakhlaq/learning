//import { secret } from '@/server/routes/secret/secret';
import type { ContextVariables } from "@/server/types";
import { OpenAPIHono } from "@hono/zod-openapi";
import { secret } from "@/server/routes/products/secret";

export const secretApp = new OpenAPIHono<{ Variables: ContextVariables }>()

  //middleware
  .use(async (c, next) => {
    const user = c.get("user");
    if (!user) {
      c.status(401);
      return c.body(null);
    }

    return next();
  })
  //route
  .route("/secret", secret);