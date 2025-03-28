import { useQueries, useQuery } from "@tanstack/react-query";
import { getTodoQueryOptions } from "@/queryOptions/getTodoQuery";

type Props = {};

/*
If you have multiple queries, and you want to run them in parreller than you can use "useQueries"
 */
export default function TanUseQueries({}: Props) {
  const [result1, result2, result3] = useQueries({
    queries: [
      {
        queryKey: ["test"],
        queryFn: () => {
          Promise.resolve(10);
        },
      },
      getTodoQueryOptions(33),
      getTodoQueryOptions(32),
      //getUserQueryOptions(33),
    ],
  }); //and you pass here multiple queries each object return the query object you have in useQuery

  /*
        const [result1, result2, result3] = useSuspanceQueries({
        queries: [{
            queryKey: ["test"],
            queryFn: () => {
                Promise.resolve(10)
            },
        },
            getTodoQueryOptions(33),
            getTodoQueryOptions(32),
            //getUserQueryOptions(33),
        ]
    })//and you pass here multiple queries each object return the query object you have in useQuery
     */

  return <div></div>;
}