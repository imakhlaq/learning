"use client"
import {useQuery} from "@tanstack/react-query";

export default function List() {

    //refetch is used to refetch the query
    // isPending if there's no cached data and no query attempt was finished yet.
    //isFetching whenever the queryFn is executing, which includes initial pending as well as background refetch.
    //isLoading is only show the loader for the first time
    const {data, isError, isPending, isFetching, refetch} = useQuery({
        queryKey: ["todos"],
        queryFn: getTodo
    })

    return (
        <div>
            <p>{JSON.stringify(data.slice(0, 10))}</p>
            <button onClick={() => refetch()}>REtecth</button>
        </div>
    );
};

async function getTodo() {

    const res = await fetch("https://jsonplaceholder.typicode.com/todos")

    return await res.json()
}