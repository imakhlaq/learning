import { auth } from "@/auth";
import { NextResponse } from "next/server";
import {
  apiAuthPrefix,
  authRotes,
  DEFAULT_LOGIN_REDIRECT,
  publicRoutes,
} from "@/routes";

export default auth(async function (req, ctx) {
  const pathName = req.nextUrl.pathname;
  const isLoggedIn = !!req.auth; //checking if user is logged in

  //login or register api routes
  const isApiAuthRoute = pathName.startsWith(apiAuthPrefix);

  //public routes
  const isPublicRoutes = publicRoutes.includes(pathName);

  //private routes
  const isAuthRoute = authRotes.includes(pathName);

  //if user trying to call login/register routes allow him
  if (isApiAuthRoute) return NextResponse.next();

  //
  if (isAuthRoute) {
    if (isLoggedIn)
      return Response.redirect(new URL(DEFAULT_LOGIN_REDIRECT, req.nextUrl));
    return NextResponse.next();
  }
  //when user is not loggedIn and trying to visit private route redirect him to login page
  if (!isLoggedIn && !isPublicRoutes)
    return Response.redirect(new URL("/auth/login", req.nextUrl));

  return NextResponse.next();
});

//only invoke middleware for these routes
export const config = {
  matcher: [
    // Skip Next.js internals and all static files, unless found in search params
    "/((?!_next|[^?]*\\.(?:html?|css|js(?!on)|jpe?g|webp|png|gif|svg|ttf|woff2?|ico|csv|docx?|xlsx?|zip|webmanifest)).*)",
    // Always run for API routes
    "/(api|trpc)(.*)",
  ],
};