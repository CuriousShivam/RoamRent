import Link from 'next/link';
import GoogleButton from './components/GoogleAuthBtn'; // Import our new client buttons

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
                        {/* Server component embedding a specific navbar click variant */}
                        <GoogleButton variant="navbar" />
                    </nav>
                </div>
            </header>

            <main>
                {/* --- HERO SECTION --- */}
                <section className="mx-auto max-w-7xl px-4 py-16 text-center sm:px-6 sm:py-24 lg:px-8">
                    <h1 className="text-4xl font-extrabold tracking-tight text-slate-900 sm:text-6xl">
                        Your Journey. Your Price.<br />
                        <span className="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
              You Control the Rent.
            </span>
                    </h1>
                    <p className="mx-auto mt-6 max-w-2xl text-lg text-slate-600 sm:text-xl">
                        Roam Rent connects travelers with local car owners. Authenticate instantly with Google, finalize your profile config, and take control of your travels.
                    </p>
                    <div className="mt-10 flex justify-center max-w-[280px] mx-auto">
                        {/* Standard main card variant button injection */}
                        <GoogleButton variant="card" />
                    </div>
                </section>

                {/* --- HOW IT WORKS SECTION --- */}
                <section id="how-it-works" className="border-t border-slate-200 bg-white py-16 sm:py-24">
                    <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                        <div className="text-center">
                            <h2 className="text-3xl font-bold tracking-tight text-slate-900 sm:text-4xl">How Roam Rent Works</h2>
                            <p className="mt-4 text-slate-600">A fair, secure car rental ecosystem designed for everyone.</p>
                        </div>

                        <div className="mt-16 grid gap-12 sm:grid-cols-2 lg:gap-16">
                            {/* Customer Column */}
                            <div className="rounded-2xl border border-slate-100 bg-slate-50/50 p-8 shadow-sm">
                                <div className="text-3xl">🧳</div>
                                <h3 className="mt-4 text-xl font-bold text-slate-900">For Customers</h3>
                                <ul className="mt-4 space-y-3 text-slate-600 text-sm">
                                    <li className="flex items-start gap-2">🔹 <span>Authenticate safely using your standard Google Account.</span></li>
                                    <li className="flex items-start gap-2">🔹 <span>Complete your required profile parameters (Role, City, Phone) once.</span></li>
                                    <li className="flex items-start gap-2">🔹 <span>Raise trip requests and review competing cash offers from verified local drivers.</span></li>
                                </ul>
                            </div>

                            {/* Driver Column */}
                            <div className="rounded-2xl border border-slate-100 bg-slate-50/50 p-8 shadow-sm">
                                <div className="text-3xl">🔑</div>
                                <h3 className="mt-4 text-xl font-bold text-slate-900">For Car Owners & Drivers</h3>
                                <ul className="mt-4 space-y-3 text-slate-600 text-sm">
                                    <li className="flex items-start gap-2">🔸 <span>Log in with Google and activate your custom profile.</span></li>
                                    <li className="flex items-start gap-2">🔸 <span>List your vehicles and access open ride requests in your city.</span></li>
                                    <li className="flex items-start gap-2">🔸 <span>Submit custom price bids to win travel rosters directly.</span></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </section>

                {/* --- GOOGLE SIGN IN ACTION BANNER --- */}
                <section id="auth-section" className="mx-auto max-w-7xl px-4 py-16 sm:px-6 sm:py-24 lg:px-8">
                    <div className="rounded-3xl bg-gradient-to-br from-slate-900 via-indigo-950 to-blue-950 p-8 text-center text-white shadow-2xl sm:p-16 relative overflow-hidden">
                        <div className="absolute top-[-20%] left-[-20%] w-[60%] h-[60%] bg-blue-600/10 rounded-full blur-[100px] pointer-events-none"></div>

                        <h2 className="text-3xl font-extrabold tracking-tight sm:text-4xl relative z-10">Ready to join Roam Rent?</h2>
                        <p className="mx-auto mt-4 max-w-md text-indigo-200 relative z-10">
                            Access your personalized workspace hub securely. New users will be automatically routed to finalize registration steps.
                        </p>

                        <div className="mt-10 max-w-sm mx-auto relative z-10 bg-white/5 border border-white/10 p-8 rounded-2xl backdrop-blur-md">
                            {/* Inject the secondary card component loader */}
                            <GoogleButton variant="card" />
                        </div>

                    </div>
                </section>
            </main>

            <footer className="border-t border-slate-200 bg-white py-8 text-center text-xs text-slate-500">
                <p>© 2026 Roam Rent Platform Ecosystem. All Rights Reserved.</p>
            </footer>

        </div>
    );
}
