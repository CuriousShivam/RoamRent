"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { z } from "zod"; // 1. Import Zod

// 2. Define the Strict Zod Validation Schema matching your database constraints
const onboardingSchema = z.object({
    name: z.string()
        .min(3, { message: "Name must be at least 3 characters long." })
        .max(100, { message: "Name cannot exceed 100 characters." })
        .refine((val) => val.trim().length > 0, { message: "Name cannot be empty spaces." }),

    role: z.enum(["CUSTOMER", "OWNER"], {
        error: () => ({ message: "Please select a valid usage role option." })
    }),

    phone: z.string()
        // Matches Indian mobile number variants (+91, 0, or raw 10 digits) safely
        .regex(/^(?:\+91|0)?[6-9]\d{9}$/, { message: "Please enter a valid 10-digit mobile number." }),

    address: z.string()
        .min(10, { message: "Street address must be at least 10 characters long to ensure authenticity." })
        .max(300, { message: "Address cannot exceed 300 characters." }),

    city: z.string()
        .min(3, { message: "City name must be at least 2 characters." })
        .max(100),

    state: z.string()
        .min(3, { message: "State name must be at least 2 characters." })
        .max(100),
});

export default function CompleteProfilePage() {
    const router = useRouter();

    const [profile, setProfile] = useState({ name: "", email: "" });
    const [fetching, setFetching] = useState(true);

    const [formData, setFormData] = useState({
        name: "",
        role: "CUSTOMER",
        phone: "",
        address: "",
        city: "",
        state: "",
    });

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState({ text: "", isError: false });

    useEffect(() => {
        const fetchSessionData = async () => {
            try {
                const response = await fetch(process.env.NEXT_PUBLIC_BACKEND_URL + "/api/auth/me", {
                    method: "GET",
                    credentials: "include"
                });
                const result = await response.json();

                if (response.ok && result.success) {
                    setProfile({ name: result.data.name, email: result.data.email });
                    setFormData((prev) => ({ ...prev, name: result.data.name }));
                } else {
                    router.push("/");
                }
            } catch (err) {
                console.error("Backend validation loop failure", err);
            } finally {
                setFetching(false);
            }
        };
        fetchSessionData();
    }, [router]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleOnboardingSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setMessage({ text: "", isError: false });

        // 3. Trigger Zod Parsing Schema Verification Checks
        const validationResult = onboardingSchema.safeParse(formData);

        if (!validationResult.success) {
            // Extract the first explicit error message string produced by Zod rules mapping
            const firstErrorMessage = JSON.parse(validationResult.error.message)[0].message;
            setMessage({ text: firstErrorMessage, isError: true });
            return; // Terminate execution early before firing network requests
        }

        setLoading(true);

        try {
            const response = await fetch(process.env.NEXT_PUBLIC_BACKEND_URL + "/api/user/complete-profile", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({
                    // Use clean, safely validated data outputs straight out of Zod parser
                    name: validationResult.data.name.trim(),
                    role: validationResult.data.role,
                    phone: validationResult.data.phone.trim(),
                    address: validationResult.data.address.trim(),
                    city: validationResult.data.city.trim(),
                    state: validationResult.data.state.trim(),
                    email: profile.email
                }),
            });

            const result = await response.json();

            if (response.ok && result.success) {
                setMessage({ text: "Profile activated cleanly! Loading workspace...", isError: false });

                localStorage.setItem("user_role", formData.role);
                localStorage.setItem("user_name", validationResult.data.name.trim());

                setTimeout(() => {
                    if (formData.role === "OWNER") {
                        router.push("/driver");
                    } else {
                        router.push("/customer");
                    }
                }, 1500);
            } else {
                setMessage({ text: result.message || "Failed to update configuration records.", isError: true });
            }
        } catch (error) {
            setMessage({ text: "Cannot establish connection to backend database.", isError: true });
        } finally {
            setLoading(false);
        }
    };

    if (fetching) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-50">
                <div className="animate-pulse font-bold text-sm text-slate-400">Verifying secure token credentials...</div>
            </div>
        );
    }

    return (
        <div className="min-h-screen grid lg:grid-cols-12 bg-white font-sans text-slate-800">

            {/* --- LEFT DESIGN PANEL: WARM WELCOME BANNER --- */}
            <div className="flex col-span-12 lg:flex lg:col-span-4  bg-gradient-to-br from-slate-900 via-indigo-950 to-blue-950 p-12 flex-col justify-between relative overflow-hidden">
                <div className="absolute top-[-20%] left-[-20%] w-[90%] h-[90%] bg-blue-600/10 rounded-full blur-[100px] pointer-events-none"></div>

                <div className="text-xl font-black text-white tracking-tight">🚗 Roam Rent</div>

                <div className="space-y-4 relative z-10">
                    <div className="text-4xl">👋✨</div>
                    <h1 className="text-3xl font-black text-white tracking-tight leading-tight">
                        Welcome to the family,<br />
                        <span className="text-blue-400">{profile.name}!</span>
                    </h1>
                    <p className="text-xs text-slate-400 leading-relaxed max-w-xs">
                        We managed to sync your identity details securely using your Google Account ({profile.email}). Let&#39;s get your profile finalized.
                    </p>
                </div>

                <div className="text-[10px] text-slate-600 font-medium tracking-wide">ROAM RENT ONBOARDING PIPELINE</div>
            </div>

            {/* --- RIGHT DESIGN PANEL: FORM INTERFACE INTERACTION --- */}
            <div className="col-span-12 lg:col-span-8 flex items-center justify-center p-6 sm:p-12 bg-slate-50/50">
                <div className="w-full max-w-xl bg-white p-8 sm:p-10 rounded-3xl border border-slate-100 shadow-xl shadow-slate-200/40">

                    <div className="mb-6">
                        <h2 className="text-2xl font-black text-slate-900 tracking-tight">Complete Your Profile</h2>
                        <p className="text-xs text-slate-400 mt-1">Provide authentic details to experience our best services.</p>
                    </div>

                    {message.text && (
                        <div className={`mb-6 p-4 rounded-xl text-xs font-bold border ${message.isError ? "bg-red-50 text-red-600 border-red-100" : "bg-green-50 text-green-600 border-green-100"}`}>
                            {message.text}
                        </div>
                    )}

                    <form className="space-y-5" onSubmit={handleOnboardingSubmit}>

                        {/* Account Role Definition Toggles */}
                        <div className="space-y-2">
                            <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">Account Usage Mode</label>
                            <div className="grid grid-cols-2 gap-3">
                                <button
                                    type="button"
                                    title="Customer"
                                    className={`py-3.5 text-xs font-bold rounded-xl border transition-all ${formData.role === "CUSTOMER" ? "bg-slate-900 border-slate-900 text-white shadow-md shadow-slate-900/10" : "bg-white border-slate-200 text-slate-600 hover:bg-slate-50"}`}
                                    onClick={() => setFormData((prev) => ({ ...prev, role: "CUSTOMER" }))}
                                >
                                    🧳 Book Cars for Trips
                                </button>
                                <button
                                    type="button"
                                    title="Driver"
                                    className={`py-3.5 text-xs font-bold rounded-xl border transition-all ${formData.role === "OWNER" ? "bg-slate-900 border-slate-900 text-white shadow-md shadow-slate-900/10" : "bg-white border-slate-200 text-slate-600 hover:bg-slate-50"}`}
                                    onClick={() => setFormData((prev) => ({ ...prev, role: "OWNER" }))}
                                >
                                    🚗 List My Vehicle for Rent
                                </button>
                            </div>
                        </div>

                        {/* Profile Contact Identifiers */}
                        <div className="space-y-4">
                            <div className="space-y-1.5">
                                <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">User Name</label>
                                <input name="name" type="tel" required placeholder="Shivam Kumar Sah" value={formData.name} onChange={handleChange} className="w-full px-4 py-3.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 focus:bg-white bg-slate-50/40 transition" />
                            </div>
                            <div className="space-y-1.5">
                                <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">Contact Phone Number</label>
                                <input name="phone" type="tel" required placeholder="+91 XXXXX XXXXX" value={formData.phone} onChange={handleChange} className="w-full px-4 py-3.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 focus:bg-white bg-slate-50/40 transition" />
                            </div>

                            <div className="space-y-1.5">
                                <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">Street Address</label>
                                <input name="address" type="text" required placeholder="House/Flat No, Apartment, Landmark Locality" value={formData.address} onChange={handleChange} className="w-full px-4 py-3.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 focus:bg-white bg-slate-50/40 transition" />
                            </div>

                            <div className="grid grid-cols-2 gap-3">
                                <div className="space-y-1.5">
                                    <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">City</label>
                                    <input name="city" type="text" required placeholder="e.g. Delhi" value={formData.city} onChange={handleChange} className="w-full px-4 py-3.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 focus:bg-white bg-slate-50/40 transition" />
                                </div>
                                <div className="space-y-1.5">
                                    <label className="text-[11px] font-bold text-slate-400 uppercase tracking-wider block">State</label>
                                    <input name="state" type="text" required placeholder="e.g. Delhi" value={formData.state} onChange={handleChange} className="w-full px-4 py-3.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 focus:bg-white bg-slate-50/40 transition" />
                                </div>
                            </div>
                        </div>

                        <div className="pt-3">
                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full py-4 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-slate-200 disabled:text-slate-400 text-sm font-bold rounded-xl transition duration-200 flex justify-center items-center shadow-lg shadow-blue-500/10 cursor-pointer"
                            >
                                {loading ? "Activating Operational Workspace..." : "Complete Registration Setup"}
                            </button>
                        </div>

                    </form>
                </div>
            </div>

        </div>
    );
}