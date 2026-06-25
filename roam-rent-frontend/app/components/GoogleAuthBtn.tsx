"use client";

export default function GoogleButton({ variant = "card" }) {
    const handleGoogleLogin = () => {
        const rootUrl = process.env.NEXT_PUBLIC_GOOGLE_AUTH_URI;
        const options = {
            client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID,
            redirect_uri: process.env.NEXT_PUBLIC_REDIRECT_URI,
            response_type: "code",
            scope: "openid email profile",
            access_type: "online",
            prompt: "select_account"
        };

        const qs = new URLSearchParams(options).toString();
        window.location.href = `${rootUrl}?${qs}`;
    };

    // Render a minimal header button layout or a large dashboard card button layout
    if (variant === "navbar") {
        return (
            <button onClick={handleGoogleLogin} className="rounded-full bg-blue-600 px-5 py-2 text-sm font-semibold text-white shadow-md shadow-blue-200 hover:bg-blue-700 transition cursor-pointer">
                Sign In
            </button>
        );
    }

    return (
        <button
            onClick={handleGoogleLogin}
            className="w-full flex items-center justify-center gap-3 bg-white text-slate-900 hover:bg-slate-50 active:scale-[0.99] font-bold text-sm py-4 px-6 rounded-xl transition duration-200 cursor-pointer shadow-xl shadow-slate-950/20"
        >
            <svg className="h-5 w-5" viewBox="0 0 24 24">
                <path fill="#4285F4" d="M23.745 12.27c0-.7-.06-1.4-.19-2.07H12v3.92h6.61c-.3 1.57-1.18 2.9-2.52 3.79v3.15h4.07c2.39-2.21 3.77-5.46 3.77-8.79z"/>
                <path fill="#34A853" d="M12 24c3.24 0 5.97-1.08 7.96-2.91l-4.07-3.15c-1.13.76-2.58 1.21-3.89 1.21-3.02 0-5.58-2.04-6.5-4.79H1.31v3.25C3.29 21.6 7.43 24 12 24z"/>
                <path fill="#FBBC05" d="M5.5 14.36c-.25-.75-.38-1.56-.38-2.36s.13-1.61.38-2.36V6.39H1.31A11.94 11.94 0 0 0 0 12c0 1.99.49 3.86 1.31 5.61l4.19-3.25z"/>
                <path fill="#EA4335" d="M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.96 1.19 15.24 0 12 0 7.43 0 3.29 2.4 1.31 6.39l4.19 3.25c.92-2.75 3.48-4.79 6.5-4.79z"/>
            </svg>
            Continue with Google
        </button>
    );
}
