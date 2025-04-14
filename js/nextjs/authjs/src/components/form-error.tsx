import {FaExclamation} from "react-icons/fa";

type Props = {
    message?: string;
};
export default function FormError({message}: Props) {
    if (!message) return null;

    return (
        <div className="bg-destructive/15 p-3 rounded-md flex items-center gap-2 text-sm text-destructive">
            <FaExclamation className={"h-4 w-4"}/>
            <p>{message}</p>
        </div>
    );
}