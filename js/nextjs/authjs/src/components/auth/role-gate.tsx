"use client";
//this will be wrapper for components that is for a specific role

import React from "react";
import { useSession } from "next-auth/react";
import FormError from "@/components/form-error";

type Props = {
  children: React.ReactNode;
  allowedRoles: string;
};
export default function RoleGate({ children, allowedRoles }: Props) {
  //current user role
  const session = useSession();
  const role = session.data?.user.role;

  if (role !== allowedRoles) {
    return (
      <FormError message={"You don't have permission to view this content"} />
    );
  }

  return <>{children}</>;
}