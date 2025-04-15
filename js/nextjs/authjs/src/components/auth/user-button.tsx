"use client";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { FaUser } from "react-icons/fa";
import { useSession } from "next-auth/react";
import LoginButton from "@/components/auth/login-button";
import { BiExit } from "react-icons/bi";

type Props = {};
export default function UserButton({}: Props) {
  const { data } = useSession();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <Avatar>
          <AvatarImage src={data?.user?.image || ""} />
          <AvatarFallback className="bg-sky-400">
            <FaUser className="text-white" />
          </AvatarFallback>
        </Avatar>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-40" align={"end"}>
        <LoginButton>
          <DropdownMenuItem>
            <BiExit className="h-4 mr-2" />
            LogOut
          </DropdownMenuItem>
        </LoginButton>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}