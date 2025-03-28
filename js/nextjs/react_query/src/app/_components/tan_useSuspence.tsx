import {getTodoQueryOptions} from "@/queryOptions/getTodoQuery";
import {useSuspenseQuery} from "@tanstack/react-query";
import {useState} from "react";

type Props = {};

//same as useQuery but the data will be always be defined
//and u should wrap this component with a suspense boundary
//<Suspense fallback={<p>Loading</>}>
// <TanUseSuspense/>
// </Suspense>
export default function TanUseSuspense({}: Props) {

    const [id, setId] = useState(1);

    const {data, isPending, refetch} = useSuspenseQuery({
        ...getTodoQueryOptions(id),
        //enable:false  //u can't use enable in useSuspense
    });

    return (
        <div></div>
    );
};