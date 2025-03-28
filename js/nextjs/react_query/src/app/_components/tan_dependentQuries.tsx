import {useState} from "react";
import {useQuery} from "@tanstack/react-query";

type Props = {};
export default function TanDependentQuries({}: Props) {
    const [isOn, setIsOn] = useState(false);

    const {data} = useQuery({
        queryKey: ["test"],
        queryFn: () => {
        },

        enabled: isOn   //when this state changes to true then query will be automatically fetched
    })

    return (
        <div></div>
    );
};