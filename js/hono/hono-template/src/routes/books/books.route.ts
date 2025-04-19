import { createRoute } from "@hono/zod-openapi";
import { UNPROCESSABLE_ENTITY } from "stoker/http-status-codes";
import { z } from "zod";

import { createRouter } from "@/lib/create-app";

const router = createRouter().openapi(
  createRoute({
    method: "post",
    path: "/add-book",
    request: {
      body: {
        content: {
          "application/json": {
            schema: z.object({
              title: z.string(),
            }),
          },
        },
      },
    },
    responses: {
      200: {
        content: {
          "application/json": {
            schema: z.object({
              message: z.string(),
            }),
          },
        },
        description: "Task API index",
      },
      // return of error
      422: {
        description: "Task API index",
      },
      // 400:{}
    },
  }),
  // handler
  (c) => {
    const valididatedBody = c.req.valid("json");
    // accessing logger inside the handler
    c.var.logger.info("info");
    return c.json({ message: "hello" });
  },
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