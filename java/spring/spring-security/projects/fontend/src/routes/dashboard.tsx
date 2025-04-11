import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useState } from "react";

export const Route = createFileRoute("/dashboard")({
  component: Dashboard,
});

function Dashboard() {
  const LOGIN_URL = "http://localhost:8085/user/test";
  const [userData, setUserData] = useState();

  useEffect(() => {
    (async function () {
      try {
        const res = await fetch(LOGIN_URL, {
          method: "GET",
          credentials: "include",
        });
        const data = await res.json();
        console.log(data);
        setUserData(data);
      } catch (e) {
        console.log(e);
      }
    })();
  }, []);

  if (userData == undefined) return;

  return (
    <div className="p-2">
      <h3>{JSON.stringify(userData)}</h3>
    </div>
  );
}