"use client";
import Image from "next/image";
import { useEffect, useState } from "react";

export default function Home() {
  const [input, setInput] = useState<string>("");
  const [searchResults, setSearchResults] = useState<{
    results: string[];
    duration: number;
  }>();

  useEffect(() => {
    async function fetchData() {
      if (!input) return setSearchResults(undefined);

      const res = await fetch(`/api/search?q=${input}}`);
    }

    fetchData();
  }, [input]);

  return (
    <div>
      <input
        type={"text"}
        value={input}
        onChange={(e) => setInput(e.target.value)}
      />
    </div>
  );
}