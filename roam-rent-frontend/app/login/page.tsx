"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function LoginPage() {
    const router = useRouter();

    // 1. Set up login form credentials state
    const [credentials, setCredentials] = useState({
        email: "",
        password: "",
    });

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ text: "", isError: false });

    // 2. Track text box entry changes
    const handleChange = (e : React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setCredentials((prev) => ({ ...prev, [name]: value }));
    };

    // 3. Send data to your Java AuthServlet
    const handleLogin = async (e : React.SubmitEvent ) => {
        e.preventDefault();
        setLoading(true);
        setMessage({ text: "", isError: false });

        try {
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(credentials),
            });

            const result = await response.json();

            if (response.ok && result.success) {
                setMessage({ text: "Login successful! Entering dashboard...", isError: false });

                // 4. CRITICAL STEP: Save the active user session into the browser memory
                localStorage.setItem("user_id", result.data.id);
                localStorage.setItem("user_name", result.data.name);
                localStorage.setItem("user_role", result.data.role);
                localStorage.setItem("user_city", result.data.city);

                // 5. Intelligent Dashboard Routing based on User Role
                setTimeout(() => {
                    if (result.data.role === "DRIVER") {
                        router.push("/driver/dashboard");
                    } else {
                        router.push("/customer/dashboard");
                    }
                }, 1500);

            } else {
                setMessage({ text: result.message || "Invalid credentials.", isError: true });
            }
        } catch (error) {
            setMessage({ text: "Cannot reach the Java authentication server.", isError: true });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-50 via-blue-50/30 to-indigo-50/50 py-12 px-4 sm:px-6 lg:px-8">

            {/* Dynamic Main Interactive Card Component */}
            <div className="max-w-md w-full space-y-8 bg-white p-8 sm:p-10 rounded-3xl shadow-xl shadow-slate-100 border border-slate-100/80 transform hover:scale-[1.01] transition-all duration-300">

                {/* Header Title Section */}
                <div className="text-center">
                    <Link href="/" className="inline-block text-2xl font-black tracking-tight text-blue-600 mb-2 hover:opacity-90 transition">
                        🚗 Roam Rent
                    </Link>
                    <h2 className="text-3xl font-extrabold tracking-tight text-slate-900">
                        Welcome Back!
                    </h2>
                    <p className="mt-2 text-sm text-slate-600">
                        Don&#39;t have an account?{" "}
                        <Link href="/register" className="font-semibold text-blue-600 hover:text-blue-500 transition underline decoration-blue-200 decoration-2 underline-offset-4">
                            Get registered here
                        </Link>
                    </p>
                </div>

                {/* Dynamic Alert Feedback Banner Blocks */}
                {message.text && (
                    <div className={`p-4 rounded-2xl text-sm font-semibold animate-pulse ${message.isError ? "bg-red-50 text-red-600 border border-red-100" : "bg-green-50 text-green-600 border border-green-100"}`}>
                        {message.text}
                    </div>
                )}

                <form className="mt-2  space-y-5" onSubmit={handleLogin}>

                    <div className="space-y-4">
                        {/* Email Box */}
                        <div className="relative group">
                            <label className="block text-slate-600 text-xs font-bold  uppercase tracking-wider mb-2 group-focus-within:text-blue-500 transition">
                                Email Address
                            </label>
                            <input
                                name="email"
                                type="email"
                                required
                                placeholder="you@example.com"
                                value={credentials.email}
                                onChange={handleChange}
                                className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm bg-slate-50/50 focus:bg-white focus:outline-none focus:border-blue-500 focus:shadow-md focus:shadow-blue-500/5 transition duration-200"
                            />
                        </div>

                        {/* Password Box */}
                        <div className="relative group">
                            <div className="flex justify-between items-center mb-2">
                                <label className="block text-slate-600 text-xs font-bold  uppercase tracking-wider group-focus-within:text-blue-500 transition">
                                    Password
                                </label>
                                <a href="#" className="text-xs font-medium text-slate-400 hover:text-blue-500 transition">
                                    Forgot?
                                </a>
                            </div>
                            <input
                                name="password"
                                type="password"
                                required
                                placeholder="••••••••••••"
                                value={credentials.password}
                                onChange={handleChange}
                                className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm bg-slate-50/50 focus:bg-white focus:outline-none focus:border-blue-500 focus:shadow-md focus:shadow-blue-500/5 transition duration-200"
                            />
                        </div>
                    </div>

                    {/* Core Interactive Login Button */}
                    <div className="pt-5">
                        <button
                            type="submit"
                            disabled={loading}
                            className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-bold rounded-xl text-white bg-blue-600 hover:bg-blue-700 disabled:bg-slate-200 disabled:text-slate-400 shadow-lg shadow-blue-500/20 disabled:shadow-none transition-all duration-200 cursor-pointer"
                        >
                            {loading ? (
                                <span className="flex items-center gap-2 -scale-50">
                  {/* CSS Loading Spinner */}
                  {/*                  <svg className="animate-spin h-5 w-5 text-slate-400" xmlns="http://w3.org" fill="none" viewBox="0 0 24 24">*/}
                  {/*  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>*/}
                  {/*  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>*/}
                  {/*</svg>*/}
                  Verifying Credentials...
                </span>
                            ) : (
                                <span className="flex items-center gap-1">
                  Secure Log In <span className="group-hover:translate-x-1 transition-transform">→</span>
                </span>
                            )}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    );
}
