import { FaCheck } from "react-icons/fa";

type Props = {
  message?: string;
};
export default function FormSuccess({ message }: Props) {
  if (!message) return null;

  return (
    <div className="bg-emerald-500/15 p-3 rounded-md flex items-center gap-2 text-sm text-emerald-500">
      <FaCheck className={"h-4 w-4"} />
      <p>{message}</p>
    </div>
  );
}