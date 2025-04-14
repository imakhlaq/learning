import React from "react";

type Props = {
  children: React.ReactNode;
};
export default function Layout({ children }: Props) {
  return (
    <div className={"h-full flex items-center justify-center bg-black/80"}>
      {children}
    </div>
  );
}