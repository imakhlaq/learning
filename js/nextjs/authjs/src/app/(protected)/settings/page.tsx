"use client";
import { useSession, signOut } from "next-auth/react";
import { Button } from "@/components/ui/button";

type Props = {};
//inside client component
export default async function Page({}: Props) {
  const { data } = useSession();

  function clickHandler() {
    signOut();
  }

  return (
    <div>
      {JSON.stringify(data)}

      <Button type={"submit"} onClick={clickHandler}>
        Sign Out
      </Button>
    </div>
  );
}

/*

=== inside server componetns
import { auth, signOut } from "@/auth";
type Props = {};
export default async function Page({}: Props) {
    const session = await auth();

    return (
        <div>
            {JSON.stringify(session)}
            {/!* server actions inside the actions*!/}
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
}*/