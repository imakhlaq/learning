import { usePathname } from "next/navigation";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import UserButton from "@/components/auth/user-button";

type Props = {};
export default function Navbar({}: Props) {
  const pathName = usePathname();

  return (
    <nav className="bg-secondary flex justify-between items-center p-4 rounded-2xl w-[600px] shadow-md">
      <div className="flex gap-2">
        <Button
          asChild
          variant={pathName === "/server" ? "default" : "outline"}
        >
          <Link href={"/server"}>Server</Link>
        </Button>
        <Button
          asChild
          variant={pathName === "/client" ? "default" : "outline"}
        >
          <Link href={"/client"}>Client</Link>
        </Button>

        <Button asChild variant={pathName === "/admin" ? "default" : "outline"}>
          <Link href={"/admin"}>Admin</Link>
        </Button>

        <Button
          asChild
          variant={pathName === "/settings" ? "default" : "outline"}
        >
          <Link href={"/settings"}>Settings</Link>
        </Button>
      </div>
      <UserButton />
    </nav>
  );
}