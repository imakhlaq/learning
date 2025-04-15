import { auth } from "@/auth";
import UserInfo from "@/components/auth/user-Info";

type Props = {};
export default async function ServerPage({}: Props) {
  const session = await auth();
  return <UserInfo user={session?.user} label={"Server Component"}></UserInfo>;
}