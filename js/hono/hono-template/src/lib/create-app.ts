import { OpenAPIHono } from "@hono/zod-openapi";
import { cors } from "hono/cors";
import { UNPROCESSABLE_ENTITY } from "stoker/http-status-codes";
import { notFound, onError, serveEmojiFavicon } from "stoker/middlewares";

import type { AppBindings } from "@/lib/types";

import { pinoLogger } from "@/middleware/pino-logger";

export function createRouter() {
  return new OpenAPIHono<AppBindings>({
    strict: false, // /get-card and /get-card/  (going to be same)

    defaultHook: (result, c) => {
      // handle zod validation error
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
  });
}

export default function createApp() {
  const app = createRouter();

  app.use(
    cors({
      origin: [
        "https://example.com",
        "https://example.org",
        "http://localhost:3000",
      ],
      allowHeaders: ["X-Custom-Header", "Upgrade-Insecure-Requests"],
      allowMethods: ["POST", "GET", "OPTIONS"],
      exposeHeaders: ["Content-Length", "X-Kuma-Revision"],
      maxAge: 600,
      credentials: true,
    }),
  );

  // add db context or auth session
  app.use(async (c, next) => {
    c.set("db", ""); // set the db instance

    // c.set("user", user);
    // c.set("session", session);
    return next();
  });

  // for any request /favicon
  app.use(serveEmojiFavicon("🔥"));
  // for logger
  app.use(pinoLogger());

  app.notFound(notFound); // when request for a route that is not available
  app.onError(onError); // when there is an error
  return app;
}