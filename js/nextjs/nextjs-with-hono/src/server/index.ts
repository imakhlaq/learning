import { OpenAPIHono, createRoute, z } from "@hono/zod-openapi";
import { ContextVariables } from "@/server/types";
import { cors } from "hono/cors";
import { apiReference } from "@scalar/hono-api-reference";
import { secretApp } from "@/server/routes/products";
import { searchApp } from "@/server/routes/search";

//you can validate values and types using Zod and generate OpenAPI Swagger documentation
const app = new OpenAPIHono<{ Variables: ContextVariables }>({
  //default validation failed handler
  defaultHook: (result, c) => {
    if (!result.success) {
      return c.json(
        {
          ok: false,
          //          errors: formatZodErrors(result),
          source: "custom_error_handler",
        },
        422,
      );
    }
  },
}).basePath("/api/v1");

app.use(
  cors({
    origin: ["https://example.com", "https://example.org"],
    allowHeaders: ["X-Custom-Header", "Upgrade-Insecure-Requests"],
    allowMethods: ["POST", "GET", "OPTIONS"],
    exposeHeaders: ["Content-Length", "X-Kuma-Revision"],
    maxAge: 600,
    credentials: true,
  }),
);

//middleware
app.use(async (c, next) => {
  c.set("db", ""); //set the db instance

  // c.set("user", user);
  // c.set("session", session);
  return next();
});

// The OpenAPI documentation will be available at /doc
app.doc31("/api/swagger.json", {
  openapi: "3.1.0",
  info: { title: "Hono x Lucia", version: "1.0.0" },
});

app.get(
  "/api/scalar",
  apiReference({
    url: "/api/swagger.json",
  }),
);

const userRoutes = createRoute({
  method: "post",
  path: "/books",
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
      description: "Success message",
    },
  },
});

//methods will be decided by the method inside the "userRoutes"
app.openapi(
  userRoutes,
  (c) => {
    const validatedBody = c.req.valid("json");
    return c.json(validatedBody); // validatedBody is {}
  },
  //over-riding the default valid failed handler
  (result, c) => {
    if (!result.success) {
      return c.json(
        {
          ok: false,
          source: "routeHook" as const,
        },
        400,
      );
    }
  },
);

const routes = app.route("/", searchApp).route("/", secretApp);

export type AppType = typeof routes;

export default app;