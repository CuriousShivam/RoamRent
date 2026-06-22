"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function RegisterPage() {
  const router = useRouter();

  // 1. Set up form state matching your PostgreSQL columns
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    role: "CUSTOMER", // Default role
    address: "",
    city: "",
    state: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: "", isError: false });

  // 2. Track input value changes
  const handleChange = (e : React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 3. Send data to your Java AuthServlet
  const handleSubmit = async (e : React.SubmitEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: "", isError: false });

    try {
      const response = await fetch( process.env.NEXT_PUBLIC_BACKEND_URL + "/api/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      const result = await response.json();

      if (response.ok && result.success) {
        setMessage({ text: "Account created successfully! Redirecting...", isError: false });
        // Redirect to login page after 2 seconds
        setTimeout(() => {
          router.push("/login");
        }, 2000);
      } else {
        setMessage({ text: result.message || "Registration failed.", isError: true });
      }
    } catch (error) {
      setMessage({ text: "Cannot connect to Java backend server.", isError: true });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-3xl shadow-xl border border-slate-100">

        <div>
          <h2 className="mt-2 text-center text-3xl font-extrabold text-slate-900">
            Create your account
          </h2>
          <p className="mt-2 text-center text-sm text-slate-600">
            Or{" "}
            <Link href="/login" className="font-medium text-blue-600 hover:text-blue-500">
              sign in to your existing profile
            </Link>
          </p>
        </div>

        {/* --- STATUS MESSAGE ALERT BANNERS --- */}
        {message.text && (
          <div className={`p-4 rounded-xl text-sm font-semibold ${message.isError ? "bg-red-50 text-red-600" : "bg-green-50 text-green-600"}`}>
            {message.text}
          </div>
        )}

        <form className="mt-8 space-y-4" onSubmit={handleSubmit}>

          {/* --- ROLE TOGGLE BLOCK (Customer vs Driver) --- */}
          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">
              I want to join as a:
            </label>
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                className={`py-3 text-sm font-bold rounded-xl border transition ${formData.role === "CUSTOMER" ? "bg-blue-50 border-blue-500 text-blue-600 shadow-sm" : "bg-white border-slate-200 text-slate-600 hover:bg-slate-50"}`}
                onClick={() => setFormData((prev) => ({ ...prev, role: "CUSTOMER" }))}
              >
                🧳 Customer
              </button>
              <button
                type="button"
                className={`py-3 text-sm font-bold rounded-xl border transition ${formData.role === "DRIVER" ? "bg-blue-50 border-blue-500 text-blue-600 shadow-sm" : "bg-white border-slate-200 text-slate-600 hover:bg-slate-50"}`}
                onClick={() => setFormData((prev) => ({ ...prev, role: "DRIVER" }))}
              >
                🚗 Car Driver
              </button>
            </div>
          </div>

          {/* --- PROFILE DATA INPUT FIELDS --- */}
          <div className="space-y-3">
            <input name="name" type="text" required placeholder="Full Name" value={formData.name} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
            <input name="email" type="email" required placeholder="Email Address" value={formData.email} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
            <input name="password" type="password" required placeholder="Password" value={formData.password} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
            <input name="phone" type="tel" required placeholder="Phone Number" value={formData.phone} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
            <input name="address" type="text" placeholder="Street Address (Optional)" value={formData.address} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />

            <div className="grid grid-cols-2 gap-3">
              <input name="city" type="text" required placeholder="City" value={formData.city} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
              <input name="state" type="text" required placeholder="State" value={formData.state} onChange={handleChange} className="w-full px-4 py-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-blue-500 transition text-slate-600" />
            </div>
          </div>

          {/* --- SUBMIT ACTIONS TRIGGER --- */}
          <div className="pt-2">
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-bold rounded-xl text-white bg-blue-600 hover:bg-blue-700 focus:outline-none disabled:bg-slate-300 shadow-md shadow-blue-100 disabled:shadow-none transition"
            >
              {loading ? "Registering Account..." : "Create Account"}
            </button>
          </div>

        </form>
      </div>
    </div>
  );
}
