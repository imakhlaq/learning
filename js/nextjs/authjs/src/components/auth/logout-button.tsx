import React from "react";
import { auth, signOut } from "@/auth";

type Props = {
  children: React.ReactNode;
};
export default function LogoutButton({ children }: Props) {
  async function logoutHandler() {
    "use server";
    await signOut();
  }

  return (
    <span onClick={logoutHandler} className="cursor-pointer">
      {children}
    </span>
  );
}