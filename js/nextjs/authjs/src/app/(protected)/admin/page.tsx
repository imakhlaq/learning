"use client";
import { useSession } from "next-auth/react";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import RoleGate from "@/components/auth/role-gate";
import FormSuccess from "@/components/form-success";

/*
If you have apis/ actions that can be only used by the admins only then
-> then you can simply use "const session=auth()" t0 get the session and base on that you can perform role check.
 */

type Props = {};
export default function AdminPage({}: Props) {
  //you can also make a custom hook that can give you currently logged-in user
  const session = useSession();
  const role = session.data?.user.role;

  return (
    <Card className={"w-[600px]"}>
      <CardHeader>
        <p className={"text-2xl font-semibold text-center"}>Admin</p>
      </CardHeader>
      <CardContent className={"space-y-4"}>
        {/* only admin will see this role gate child */}
        <RoleGate allowedRoles={"admin"}>
          <FormSuccess message={"You're allowed to view this message"} />
        </RoleGate>
      </CardContent>
    </Card>
  );
}