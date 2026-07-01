package servlet;

import com.google.gson.Gson;
import config.MyDB;
import dao.SessionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

@WebServlet("/api/v1/complete-profile")
public class CompleteProfileServlet extends HttpServlet {
    User user;
    private final Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        SessionDAO sessionDAO = new SessionDAO();
        String sessionId = sessionDAO.getSessionId(req,resp);
        // 2. Validate token against Redis using your SessionDAO
        Map<String, String> sessionData = sessionDAO.getSessionData(sessionId);
        if (sessionData == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Session expired or invalid. Please login again.\"}");
            return;
        }

        String userIdStr = sessionData.get("userId");

        Long userId = Long.parseLong(userIdStr);

        try {
            // 3. Parse Next.js incoming JSON payload
            // Read the incoming JSON body string
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }

            // Map the JSON to our Java TripRequest model
            User profileData = gson.fromJson(jsonBuffer.toString(), User.class);

            // 4. Update your application User Database using verified Redis userId
            String query = "UPDATE users SET name=?, role=?, phone=?, address=?, city=?, state=? WHERE id=?";

            try (Connection conn = MyDB.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1,  profileData.getName());
                stmt.setString(2,  profileData.getRole());
                stmt.setString(3,  profileData.getPhone());
                stmt.setString(4,  profileData.getAddress());
                stmt.setString(5, profileData.getCity());
                stmt.setString(6, profileData.getState());

                stmt.setLong(7, userId); // Secured bound parameter from Redis metadata

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("{\"success\": true, \"message\": \"Profile synchronized effectively!\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"User record matching this session id not found.\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Internal server parsing or DB error.\"}");
        }
    }
}
