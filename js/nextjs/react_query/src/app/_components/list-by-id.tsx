import {useState} from "react";
import {useQuery, useSuspenseQuery} from "@tanstack/react-query";
import {getTodoQueryOptions} from "@/queryOptions/getTodoQuery";

type Props = {};
export default function ListById({}: Props) {

    const [id, setId] = useState(1);

    //const {data, isPending, refetch} = useQuery(getTodoQueryOptions(id))

    //same as useQuery but the data will be always be defined
    const {data, isPending, refetch} = useSuspenseQuery(getTodoQueryOptions(id));

    return (
        <div></div>
    );
};