import { createFileRoute } from "@tanstack/react-router";
import Login from "../components/login.tsx";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return (
    <div className="p-2">
      <Login></Login>
    </div>
  );
}