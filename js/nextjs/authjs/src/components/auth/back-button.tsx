"use client";
import { Button } from "@/components/ui/button";
import Link from "next/link";

type Props = {
  label: string;
  href: string;
};
export default function BackButton({ href, label }: Props) {
  return (
    <Button
      variant={"link"}
      className={"font-normal w-full"}
      size={"sm"}
      asChild
    >
      <Link href={href}>{label}</Link>
    </Button>
  );
}