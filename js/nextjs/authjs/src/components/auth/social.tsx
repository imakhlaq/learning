"use client";
import { Button } from "@/components/ui/button";
import { FaGoogle } from "react-icons/fa";
import { FaGithub } from "react-icons/fa";
import { signIn } from "next-auth/react";
import { DEFAULT_LOGIN_REDIRECT } from "@/routes";

type Props = {};
export default function Social({}: Props) {
  async function clickHandler(provider: "google" | "github") {
    //when you want to log in from a client component
    //import signin from "next-auth/react"
    await signIn("google", {
      redirectTo: DEFAULT_LOGIN_REDIRECT,
    });
  }

  return (
    <div className={"flex items-center justify-center w-full gap-x-4"}>
      <Button
        size={"lg"}
        className={"shadow-chart-3 w-30 cursor-pointer"}
        variant={"outline"}
        onClick={() => clickHandler("google")}
      >
        <FaGoogle className={"h-5 w-5"} />
      </Button>

      <Button
        size={"lg"}
        className={"shadow-chart-3 w-30 cursor-pointer"}
        variant={"outline"}
        onClick={() => clickHandler("github")}
      >
        <FaGithub className={"h-5 w-5"} />
      </Button>
    </div>
  );
}