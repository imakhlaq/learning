"use client"

import {QueryClient} from "@tanstack/query-core";
import {QueryClientProvider} from "@tanstack/react-query";
import React from "react";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            gcTime: 1000 * 60 * 60 * 24, // 24 hours
        },
    },
})

type Props = {
    children: React.ReactNode
}

export default function StoreProvider({children}: Props) {
    return (
        <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    );
};