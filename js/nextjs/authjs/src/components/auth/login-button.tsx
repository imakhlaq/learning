"use client";

import { useRouter } from "next/navigation";

interface LoginButtonProps {
  children: React.ReactNode;
  mode?: "model" | "redirect";
  asChild?: boolean;
}

export default function LoginButton({
  children,
  mode = "redirect",
  asChild,
}: LoginButtonProps) {
  const router = useRouter();

  function clickHandler() {
    router.push("/auth/login");
  }

  if (mode == "model") {
    return <span>TODO: Implement model</span>;
  }

  return (
    <span onClick={clickHandler} className={"cursor-pointer"}>
      {children}
    </span>
  );
}