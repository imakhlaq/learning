import React from "react";
import Navbar from "@/app/(protected)/_components/navbar";

type Props = {
  children: React.ReactNode;
};
export default function ProtectedLayout({ children }: Props) {
  return (
    <div className={"h-full flex items-center justify-center bg-black/80"}>
      <Navbar />
      {children}
    </div>
  );
}