export const publicRoutes = ["/"];

export const protectedRoutes = ["/auth/login", "/auth/register"];

//all the api routes prefix with /api/auth will be available for all
export const apiAuthPrefix = "/api/auth";

export const DEFAULT_LOGIN_REDIRECT = "/settings";