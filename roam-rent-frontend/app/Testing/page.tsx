'use client'
import {useEffect} from "react";

export default function Testing  () {
    useEffect(() => {
        const callTestAPI = async () => {
            const response = await fetch(process.env.NEXT_PUBLIC_BACKEND_URL + "/api/v1/testing/session", {
                method: "GET",
                credentials: "include",
                headers:{
                    origin:"google.com",
                }
            });
        };
        callTestAPI();
    }, []);

    return "Testing";
}