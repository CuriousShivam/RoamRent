package config;

import dao.SessionDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class SessionCheckFilter implements Filter {
    private final SessionDAO sessionDAO = new SessionDAO();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = ((HttpServletRequest) req).getRequestURI();

        String sessionId = sessionDAO.getSessionId(req, res);
        if (sessionId == null) {
            return; // Fast-fail if no token present
        }

        Map<String, String> sessionData = sessionDAO.getSessionData(sessionId);
        if (sessionData == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ResponseUtil.sendJson(res, 401, "Unauthorized User", new Object());
            return;
        }

        String userId = sessionData.get("userId");
        String email = sessionData.get("email");

        if (path.endsWith("/api/v1/complete-profile")) {
            // Context data rule 1: Pass only userId and email down
            req.setAttribute("context.userId", userId);
            req.setAttribute("context.email", email);
        } else {
            // Context data rule 2: Require role check for all general endpoints
            String role = sessionData.get("role");
            if (role == null || role.isBlank()) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ResponseUtil.sendJson(res, 401, "Unauthorized User", new Object());
                return;
            }

            // Inject comprehensive authorization profile context
            req.setAttribute("context.userId", userId);
            req.setAttribute("context.email", email);
            req.setAttribute("context.role", role);
        }

        // Move securely along to your actual destination Servlet mapping
        chain.doFilter(request, response);
    }
}