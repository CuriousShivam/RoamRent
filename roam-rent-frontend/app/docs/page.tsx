"use client";

import dynamic from "next/dynamic";
import "swagger-ui-react/swagger-ui.css";

// We load the component dynamically to prevent server-side rendering (SSR) errors
const SwaggerUI = dynamic(() => import("swagger-ui-react"), { ssr: false });

export default function DocsPage() {
  return (
    <div style={{ padding: "20px", backgroundColor: "#fff", minHeight: "100vh" }}>
      <h1 style={{ textAlign: "center", marginBottom: "20px", color: "#333" }}>
        Roam Rent API Dashboard
      </h1>
      {/* This points directly to the file we saved in the public folder */}
      <SwaggerUI url="/openapi.yaml" />
    </div>
  );
}
