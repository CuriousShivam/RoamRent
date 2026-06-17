package servlet;
import com.google.gson.Gson;
import config.ResponseUtil;
import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/auth/*") // The asterisk (*) catches both /signup and /login paths
public class AuthServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // This grabs the string after "/api/auth"

        if (pathInfo == null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing endpoint path", null);
            return;
        }

        // Handle the incoming JSON string from Next.js
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        if (pathInfo.equals("/signup")) {
            handleSignup(jsonBuffer.toString(), resp);
        } else if (pathInfo.equals("/login")) {
            handleLogin(jsonBuffer.toString(), resp);
        } else {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found", null);
        }
    }

    private void handleSignup(String jsonBody, HttpServletResponse resp) throws IOException {
        // Map the incoming JSON directly to our Java User model
        User newUser = gson.fromJson(jsonBody, User.class);

        // Basic verification checks
        if (newUser.getEmail() == null || newUser.getPassword() == null || newUser.getRole() == null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required profile fields", null);
            return;
        }

        // Check if an account already uses this email
        if (userDAO.getUserByEmail(newUser.getEmail()) != null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_CONFLICT, "Email profile is already registered", null);
            return;
        }

        // Save the user data via JDBC
        boolean isSaved = userDAO.registerUser(newUser);

        if (isSaved) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_CREATED, "User registered successfully!", null);
        } else {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database registration failed", null);
        }
    }

    private void handleLogin(String jsonBody, HttpServletResponse resp) throws IOException {
        // Map login JSON fields to our template user object
        User loginCredentials = gson.fromJson(jsonBody, User.class);

        User existingUser = userDAO.getUserByEmail(loginCredentials.getEmail());

        if (existingUser == null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or account does not exist", null);
            return;
        }

        // Verify the plain text password matches (Note: Upgrading to password hashing is safer later)
        if (!existingUser.getPassword().equals(loginCredentials.getPassword())) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_UNAUTHORIZED, "Incorrect password attempt", null);
            return;
        }

        // Security clean up: Erase the password field from the response object before sending it to Next.js
        existingUser.setPassword(null);

        ResponseUtil.sendJson(resp, HttpServletResponse.SC_OK, "Login successful!", existingUser);
    }

    // Handle pre-flight browser security requests from Next.js (CORS setup)
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
