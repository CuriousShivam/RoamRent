package config; // Change to your actual package

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CorsFilter implements Filter {

    private List<String> allowedOrigins;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        // 1. Fetch the dynamic incoming origin from Next.js (e.g., http://localhost:3000)
        String origin = req.getHeader("Origin");
        System.out.println("Inside Cors Filter" + origin);
        // 2. Set the exact origin instead of using "*" (Wildcards fail when allowing credentials)
        if (origin != null && allowedOrigins != null && allowedOrigins.contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            // Fallback default domain if origin is null or untrusted
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ResponseUtil.sendJson(res, 403, "Access not allowed", new Object());
            return;
        }

        // 3. Crucial headers for handling your ROAM_SESSION cookie transfers
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Accept");
        res.setHeader("Access-Control-Max-Age", "3600");

        // 4. Handle browser CORS preflight (OPTIONS) requests immediately
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continue along the filter chain
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {
        String rawOrigins = FromENV.get("FRONTEND_URLS");
        allowedOrigins = Arrays
                .stream(rawOrigins.split(","))
                .map(String::trim)
                .toList();
    }
    @Override public void destroy() {}
}
