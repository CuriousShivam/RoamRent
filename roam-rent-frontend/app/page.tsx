import Link from 'next/link';

// 1. Define SEO Metadata for Roam Rent
export const metadata = {
    title: 'Roam Rent | Affordable Car Rentals & Driver Bidding Platform',
    description: 'Book long trips or rent out your car on Roam Rent. Customers post trip details, local car owners offer bids, and you pick the best rental price!',
    keywords: 'car rental, rent car, driver bidding, car sharing, cheap car hire, travel trip booking',
};

export default function LandingPage() {
    return (
        <div className="min-h-screen bg-slate-50 font-sans text-slate-800">

            {/* --- HEADER NAVBAR SECTION --- */}
            <header className="sticky top-0 z-50 border-b border-slate-200 bg-white/80 backdrop-blur-md">
                <div className="mx-auto flex max-w-7xl items-center justify-between p-4 sm:px-6 lg:px-8">
                    <div className="flex items-center gap-2">
                        <span className="text-2xl font-black tracking-tight text-blue-600">🚗 Roam Rent</span>
                    </div>
                    <nav className="flex items-center gap-4">
                        <Link href="#how-it-works" className="text-sm font-medium text-slate-600 hover:text-blue-600 transition">
                            How It Works
                        </Link>
                        <div className="h-4 w-px bg-slate-200"></div>
                        <Link href="#auth-section" className="rounded-full bg-blue-600 px-5 py-2 text-sm font-semibold text-white shadow-md shadow-blue-200 hover:bg-blue-700 hover:shadow-none transition">
                            Get Started
                        </Link>
                    </nav>
                </div>
            </header>

            <main>
                {/* --- HERO HERO SECTION --- */}
                <section className="mx-auto max-w-7xl px-4 py-16 text-center sm:px-6 sm:py-24 lg:px-8">
                    <h1 className="text-4xl font-extrabold tracking-tight text-slate-900 sm:text-6xl">
                        Your Journey. Your Price.<br />
                        <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
              You Control the Rent.
            </span>
                    </h1>
                    <p className="mx-auto mt-6 max-w-2xl text-lg text-slate-600 sm:text-xl">
                        Roam Rent connects travelers with local car owners. Post your destination, receive live price bids from drivers, and pick the perfect deal that fits your wallet.
                    </p>
                    <div className="mt-10 flex justify-center gap-4">
                        <Link href="#auth-section" className="rounded-xl bg-blue-600 px-8 py-4 text-base font-bold text-white shadow-xl shadow-blue-200 hover:bg-blue-700 transition">
                            Book a Ride
                        </Link>
                        <Link href="#auth-section" className="rounded-xl bg-white border border-slate-300 px-8 py-4 text-base font-bold text-slate-700 shadow-sm hover:bg-slate-50 transition">
                            List Your Car
                        </Link>
                    </div>
                </section>

                {/* --- HOW IT WORKS SECTION (SEO Rich Keyword Text) --- */}
                <section id="how-it-works" className="border-t border-slate-200 bg-white py-16 sm:py-24">
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        <div className="text-center">
                            <h2 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">How Roam Rent Works</h2>
                            <p className="mt-4 text-slate-600">A decentralized, fair car rental ecosystem designed for everyone.</p>
                        </div>

                        <div className="mt-16 grid gap-12 sm:grid-cols-2 lg:gap-16">
                            {/* Customer Column */}
                            <div className="rounded-2xl border border-slate-100 bg-slate-50/50 p-8 shadow-sm">
                                <div className="text-3xl">🧳</div>
                                <h3 className="mt-4 text-xl font-bold text-slate-900">For Customers</h3>
                                <ul className="mt-4 space-y-3 text-slate-600 text-sm">
                                    <li className="flex items-start gap-2">🔹 <span>Register securely on the web application.</span></li>
                                    <li className="flex items-start gap-2">🔹 <span>Raise a trip request by typing your pickup address, destination, passenger counts, and travel date timings.</span></li>
                                    <li className="flex items-start gap-2">🔹 <span>Receive dynamic competitive rental price offers straight from local vehicle owners.</span></li>
                                    <li className="flex items-start gap-2">🔹 <span>Confirm the best offer to instantly lock and schedule your upcoming trip dashboard tracking.</span></li>
                                </ul>
                            </div>

                            {/* Driver Column */}
                            <div className="rounded-2xl border border-slate-100 bg-slate-50/50 p-8 shadow-sm">
                                <div className="text-3xl">🔑</div>
                                <h3 className="mt-4 text-xl font-bold text-slate-900">For Car Owners & Drivers</h3>
                                <ul className="mt-4 space-y-3 text-slate-600 text-sm">
                                    <li className="flex items-start gap-2">🔸 <span>Create your driver profile and list your vehicle assets along with total seating capacities.</span></li>
                                    <li className="flex items-start gap-2">🔸 <span>View matched passenger travel requests originating strictly in your same city or same state.</span></li>
                                    <li className="flex items-start gap-2">🔸 <span>Submit custom rental price bids (Rent for trip) matching what you want to earn.</span></li>
                                    <li className="flex items-start gap-2">🔸 <span>Win bookings and view verified upcoming travel rosters instantly on your driver screen panels.</span></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </section>

                {/* --- DUAL LOGIN & SIGNUP ACTION PANEL SECTION --- */}
                <section id="auth-section" className="mx-auto max-w-7xl px-4 py-16 sm:px-6 sm:py-24 lg:px-8">
                    <div className="rounded-3xl bg-gradient-to-br from-slate-900 via-indigo-950 to-blue-950 p-8 text-center text-white shadow-2xl sm:p-16">
                        <h2 className="text-3xl font-extrabold tracking-tight sm:text-4xl">Ready to join Roam Rent?</h2>
                        <p className="mx-auto mt-4 max-w-xl text-indigo-200">
                            Create an account or login below to enter your customized dashboard interface panel.
                        </p>

                        <div className="mt-10 grid gap-4 max-w-md mx-auto sm:grid-cols-2">
                            <div className="rounded-2xl bg-white/10 border border-white/10 p-6 backdrop-blur-sm">
                                <h3 className="text-lg font-bold mb-4">New to the platform?</h3>
                                <Link href="/roam-rent-frontend/app/register" className="block w-full text-center rounded-xl bg-blue-600 py-3 text-sm font-bold text-white shadow-md hover:bg-blue-500 transition">
                                    Create Account
                                </Link>
                            </div>

                            <div className="rounded-2xl bg-white/10 border border-white/10 p-6 backdrop-blur-sm">
                                <h3 className="text-lg font-bold mb-4">Returning user?</h3>
                                <Link href="/login" className="block w-full text-center rounded-xl bg-white py-3 text-sm font-bold text-slate-900 shadow-md hover:bg-slate-100 transition">
                                    Log In
                                </Link>
                            </div>
                        </div>
                    </div>
                </section>
            </main>

            {/* --- FOOTER BANNER SECTION --- */}
            <footer className="border-t border-slate-200 bg-white py-8 text-center text-xs text-slate-500">
                <p>© 2026 Roam Rent Platform Ecosystem. All Rights Reserved. Master built using Java Servlets, Next.js, and PostgreSQL.</p>
            </footer>

        </div>
    );
}
