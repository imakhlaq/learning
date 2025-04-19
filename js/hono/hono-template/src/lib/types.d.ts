import type { OpenAPIHono, RouteConfig, RouteHandler } from "@hono/zod-openapi";
import type { PinoLogger } from "hono-pino";

export interface AppBindings {
  Variables: {
    logger: PinoLogger;
    db: any;
    user: any;
  };
}

export type AppOpenAPI = OpenAPIHono<AppBindings>;

// generic type for route handler
export type AppRouteHandler<R extends RouteConfig> = RouteHandler<
  R,
  AppBindings
>;