import { ExtendedUser } from "../../../next-auth";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

type Props = {
  user?: ExtendedUser;
  label: string;
};
export default function UserInfo({ user, label }: Props) {
  return (
    <Card className="w-[600px] shadow-md">
      <CardHeader>
        <p className="text-2xl font-semibold text-center">{label}</p>
      </CardHeader>
      <CardContent className={"space-y-4"}>
        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-md">
          <p className="text-sm font-semibold">ID</p>
          <p className="truncate text-sm max-w-[180px] font-mono p-1 bg-amber-100 rounded-md">
            {user?.id}
          </p>
        </div>

        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-md">
          <p className="text-sm font-semibold">Name</p>
          <p className="truncate text-sm max-w-[180px] font-mono p-1 bg-amber-100 rounded-md">
            {user?.name}
          </p>
        </div>

        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-md">
          <p className="text-sm font-semibold">Email</p>
          <p className="truncate text-sm max-w-[180px] font-mono p-1 bg-amber-100 rounded-md">
            {user?.email}
          </p>
        </div>

        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-md">
          <p className="text-sm font-semibold">Role</p>
          <p className="truncate text-sm max-w-[180px] font-mono p-1 bg-amber-100 rounded-md">
            {user?.role}
          </p>
        </div>

        <div className="flex flex-row items-center justify-between rounded-lg border p-3 shadow-md">
          <p className="text-sm font-semibold">2FA Status</p>
          <Badge
            variant={user?.twoFactorAuthStatus ? "secondary" : "destructive"}
          >
            {user?.twoFactorAuthStatus ? "ON" : "OFF"}
          </Badge>
        </div>
      </CardContent>
    </Card>
  );
}