import { createRoute } from "@hono/zod-openapi";

const tags = ["Tasks"];

export const list = createRoute({
  path: "/tasks",
  method: "get",
  tags,
  responses: {
    200: {
      content: {},
    },
  },

  request: {},
});

export type ListRoute = typeof list;