"use client"
import {useQuery} from "@tanstack/react-query";
import {getTodoQueryOptions} from "@/queryOptions/getTodoQuery";

/*

    refetch is used to refetch the query
    isPending if there's no cached data and no query attempt was finished yet.
    isFetching whenever the queryFn is executing, which includes initial pending as well as background refetch.
    isLoading is only show the loader for the first time
 */

export default function TanUseQuery() {

    const {data, isError, isPending, isFetching, refetch} = useQuery({
        ...getTodoQueryOptions(1),  //u can have default options in this fn,
        //and you can add more specific thing here
    })

    return (
        <div>
            <p>{JSON.stringify(data?.slice(0, 10))}</p>
            <button onClick={() => refetch()}>REtecth</button>
        </div>
    );
};