"use client";

import dynamic from "next/dynamic";
import { useEffect } from "react";
import "swagger-ui-react/swagger-ui.css";

const SwaggerUI = dynamic(() => import("swagger-ui-react"), { ssr: false });

export default function DocsPage() {
    // Suppress the warning just for this page
    useEffect(() => {
        const originalWarn = console.error;
        console.error = (...args) => {
            if (args[0]?.includes?.("UNSAFE_componentWillReceiveProps")) return;
            originalWarn(...args);
        };
        return () => { console.error = originalWarn; };
    }, []);

    return (
        <div style={{ padding: "20px", backgroundColor: "#fff", minHeight: "100vh" }}>
            <h1 style={{ textAlign: "center", marginBottom: "20px", color: "#333" }}>
                Roam Rent API Dashboard
            </h1>
            <SwaggerUI url="/openapi.yaml" />
        </div>
    );
}
