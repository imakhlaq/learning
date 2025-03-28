import { useQueryClient } from "@tanstack/react-query";

type Props = {};
export default function TanUseQueryClientDemo({}: Props) {
  const client = useQueryClient();

  //to in validate and fetch the qury again
  async function clickHandler() {
    await client.invalidateQueries({
      queryKey: ["contact"],
    });
  }

  return <div onClick={clickHandler}>$</div>;
}