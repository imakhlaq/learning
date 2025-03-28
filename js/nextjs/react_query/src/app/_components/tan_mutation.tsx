import { useMutation } from "@tanstack/react-query";

type Props = {};
export default function TanMutation({}: Props) {
  const { mutate, isPending, error, isError } = useMutation({
    mutationKey: ["edit-contact", 222],
    mutationFn: async (data: any) => {
      return await fetch("http://localhost", { method: "POST", body: data });
    },
    onSuccess: () => {}, //called when the mutation is successfully completed
    onError: () => {}, //called when mutation is unsuccessfully completed
  });

  return <div>$</div>;
}