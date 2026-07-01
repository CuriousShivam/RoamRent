package servlet;

import config.ResponseUtil;
import dao.SessionDAO;
import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/auth/me")
public class MeServlet extends HttpServlet {

    private final SessionDAO sessionDAO = new SessionDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true"); // Allows browser pre-flight checks to clear cookies
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Enforce basic CORS headers
        System.out.println("IN MESERVLET");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        String sessionId = null;
        System.out.println(req.getCookies());
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("ROAM_SESSION".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println(sessionId);

        if (sessionId == null) {

            ResponseUtil.sendJson(resp, HttpServletResponse.SC_UNAUTHORIZED, "No active session found", null);
            return;
        }

        // Pull active user session meta out of Upstash Redis
        Map<String, String> sessionData = sessionDAO.getSessionData(sessionId);
        if (sessionData == null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_UNAUTHORIZED, "Session expired", null);
            return;
        }

        // Fetch up-to-date name/email values out of PostgreSQL using the session's email
        User user = userDAO.getUserByEmail(sessionData.get("email"));
        System.out.println(user);
        if (user != null) {
            user.setPassword(null); // Clear password security hash out of the transport stream
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_OK, "Session authenticated", user);
        } else {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "User profile missing", null);
        }
    }
}