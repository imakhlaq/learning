import { FormEvent, useState } from "react";
import { useNavigate } from "@tanstack/react-router";

export default function Login() {
  const LOGIN_URL = "http://localhost:8085/user/login";

  const navigate = useNavigate();
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState("");

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();

    try {
      const res = await fetch(LOGIN_URL, {
        method: "POST",
        credentials: "include",
        body: JSON.stringify({ email, password }),
      });
      const data = await res.json();
      console.log(data);
      await navigate({ to: "/dashboard" });
    } catch (e) {
      console.log(e);
    }
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type={"text"}
          id={"email"}
          onChange={(e) => setEmail(e.target.value)}
          value={email}
        />
        <input
          type={"text"}
          id={"password"}
          onChange={(e) => setPassword(e.target.value)}
          value={password}
        />
        <button type={"submit"}>Login</button>
      </form>
    </div>
  );
}