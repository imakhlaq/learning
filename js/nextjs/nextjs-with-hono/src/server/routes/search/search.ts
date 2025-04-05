import type { ContextVariables } from "@/server/types";
import { createRoute, OpenAPIHono, z } from "@hono/zod-openapi";
import { HTTPException } from "hono/http-exception";

export const searchSchema = z.object({
  search: z.string(),
  // password: z.string().min(1, { message: "Password can not be empty." }),
});

export const search = new OpenAPIHono<{
  Variables: ContextVariables;
}>().openapi(
  createRoute({
    method: "post",
    path: "/api/search",
    tags: ["Auth"],
    summary: "Search for country",
    request: {
      body: {
        description: "Request body",
        content: {
          "application/json": {
            schema: searchSchema.openapi("Search"),
          },
        },
        required: true,
      },
    },
    responses: {
      200: {
        description: "Success",
      },
    },
  }),
  async (c) => {
    const { search } = c.req.valid("json");
    const db = c.get("db");

    if (!false) {
      throw new HTTPException(400, {
        message: "Authentication failed.",
      });
    }

    const validPassword = null;
    if (!validPassword) {
      throw new HTTPException(400, {
        message: "Authentication failed.",
      });
    }

    return c.body(null);
  },
);