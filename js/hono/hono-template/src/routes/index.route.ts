import { createRoute } from "@hono/zod-openapi";
import { UNPROCESSABLE_ENTITY } from "stoker/http-status-codes";
import * as HttpStatusCode from "stoker/http-status-codes";
import { z } from "zod";

import { createRouter } from "@/lib/create-app";

const router = createRouter().openapi(
  createRoute({
    tags: ["index"], // to group the route
    method: "get",
    path: "/",
    responses: {
      [HttpStatusCode.OK]: {
        content: {
          "application/json": {
            schema: z.object({
              message: z.string(),
            }),
          },
        },
        description: "Task API index",
      },
      [HttpStatusCode.UNPROCESSABLE_ENTITY]: {
        description: "Zod Error",
      },
      // 400:{}
    },
  }),
  // handler
  (c) => {
    // accessing logger inside the handler
    c.var.logger.info("info");
    return c.json({ message: "hello" });
  },
  // overriding the default hook that handle zod error
  (result, c) => {
    // zod validation error for each path
    if (!result.success) {
      return c.json(
        {
          success: result.success,
          error: result.error,
        },
        UNPROCESSABLE_ENTITY,
      );
    }
  },
);

export default router;