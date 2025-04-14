import { Poppins } from "next/font/google";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import LoginButton from "@/components/auth/login-button";

//adding fonts
const font = Poppins({
  subsets: ["latin"],
  weight: ["600"],
});

export default function Home() {
  return (
    <main
      className={
        "flex flex-col h-full items-center justify-center bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] bg-sky-400 to-blue-800"
      }
    >
      <div className={"space-x-6"}>
        <h1
          className={
            "text-6xl font-semibold text-white drop-shadow-md text-center"
          }
        >
          AUTH
        </h1>
        <p className={cn("text-white text-lg text-center", font.className)}>
          A Simple authentication service
        </p>
      </div>
      <div>
        <LoginButton>
          <Button variant={"custom"} size={"lg"}>
            SignIn
          </Button>
        </LoginButton>
      </div>
    </main>
  );
}