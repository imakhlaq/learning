import {queryOptions} from "@tanstack/react-query";

//select will be passed by the caller of the function
export function getTodoQueryOptions<T = Todo[]>(id: number, select?: (data: Todo[]) => T) {

    return queryOptions({
        queryKey: ["todo", id],//you need to have "id" or else all the queryKey with "post" will be treated same
        queryFn: () => getTodoById(id),//id will come from querykey
        enabled: false, //to conditionally load a query
        select: select //this function receives the data return by the queryFn, and it can transform it.
    })
}

async function getTodoById(id: number): Promise<Todo[]> {

    const res = await fetch(`https://jsonplaceholder.typicode.com/comments?postId=${id}`)

    return await res.json()
}

type Todo = {
    useId: number,
    id: number,
    title: string,
    completed: boolean
};