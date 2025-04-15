import { auth, signOut } from "@/auth";
import { Button } from "@/components/ui/button";

type Props = {};
export default async function Page({}: Props) {
  const session = await auth();

  return (
    <div>
      {JSON.stringify(session)}
      {/* server actions inside the actions*/}
      <form
        action={async () => {
          "use server";
          await signOut();
        }}
      >
        <Button type={"submit"}>Sign Out</Button>
      </form>
    </div>
  );
}