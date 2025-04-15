/*
Because of the matcher in the middleware all routes are private
 */

export const publicRoutes = ["/", "/auth/new-verification"];

//routes that can be access without auth
export const authRotes = [
  "/auth/login",
  "/auth/register",
  "/auth/error",
  "/auth/reset",
  "/auth/new-password",
];

//all the api routes prefix with /api/auth will be available for all
export const apiAuthPrefix = "/api/auth";

export const DEFAULT_LOGIN_REDIRECT = "/settings";