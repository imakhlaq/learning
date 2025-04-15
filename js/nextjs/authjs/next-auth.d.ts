import type { DefaultSession } from "next-auth";

export type ExtendedUser = DefaultSession["user"] & {
  role: string;
  twoFactorAuthStatus: boolean;
};

declare module "next-auth" {
  interface Session {
    user: {
      role: string;
      twoFactorAuthStatus: boolean;
    } & DefaultSession["user"];
  }
}

import { JWT } from "next-auth/jwt";

declare module "next-auth/jwt" {
  /** Returned by the `jwt` callback and `auth`, when using JWT sessions */
  interface JWT {
    /** OpenID ID Token */
    role: "USER" | "ADMIN" | "MANAGER" | null;
    twoFactorAuthStatus: boolean;
  }
}