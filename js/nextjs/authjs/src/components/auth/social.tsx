"use client";
import { Button } from "@/components/ui/button";
import { FaGoogle } from "react-icons/fa";
import { FaGithub } from "react-icons/fa";

type Props = {};
export default function Social({}: Props) {
  return (
    <div className={"flex items-center justify-center w-full gap-x-4"}>
      <Button
        size={"lg"}
        className={"shadow-chart-3 w-30 cursor-pointer"}
        variant={"outline"}
        onClick={() => {}}
      >
        <FaGoogle className={"h-5 w-5"} />
      </Button>

      <Button
        size={"lg"}
        className={"shadow-chart-3 w-30 cursor-pointer"}
        variant={"outline"}
        onClick={() => {}}
      >
        <FaGithub className={"h-5 w-5"} />
      </Button>
    </div>
  );
}