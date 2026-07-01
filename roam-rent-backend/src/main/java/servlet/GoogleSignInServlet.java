package servlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import config.FromENV;
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
import java.util.Objects;

@WebServlet("/api/auth/google-signin")
public class GoogleSignInServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final SessionDAO sessionDAO = new SessionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Grab the temporary validation authorization code string passed by Google in the URL
        String authorizationCode = req.getParameter("code");

        if (authorizationCode == null) {
            resp.sendRedirect("http://localhost:3000/login?error=oauth_failed");
            return;
        }

        try {
            System.out.println("Before token exchange");
            // 2. Exchange the temporary code string for real identity tokens from Google's servers
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    GoogleOAuthConstants.TOKEN_SERVER_URL,
                    FromENV.get("O_AUTH_CLIENT_ID"),
                    FromENV.get("O_AUTH_CLIENT_SECRET"),
                    authorizationCode,
                    FromENV.get("O_AUTH_REDIRECT_URL")
            ).execute();


            // 3. Extract the confirmed user identification profile
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload profile = idToken.getPayload();

            String oauthId = profile.getSubject();
            String email = profile.getEmail();
            String name = (String) profile.get("name");

            // 4. Look up or register the profile inside your PostgreSQL database : For New Users only
            User user = userDAO.getUserByOAuth("GOOGLE", oauthId);
            String sessionId;
            String redirectURL;
            if (user == null) {
                // Add the custom mapping logic
                String sqlInsert = "INSERT INTO users (name, email, oauth_provider, oauth_id) VALUES ( ?, ?, 'GOOGLE', ?)";
                try (java.sql.Connection conn = config.MyDB.getConnection();
                     java.sql.PreparedStatement stmt = conn.prepareStatement(sqlInsert, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, oauthId);
                    stmt.executeUpdate();

                    try (java.sql.ResultSet gk = stmt.getGeneratedKeys()) {
                        if (gk.next()) user.setId(gk.getLong(1));
                    }
                }

                sessionId = sessionDAO.createSession(user.getId(), user.getEmail());
                redirectURL = "http://localhost:3000/complete-profile";

            }else{
                if(Objects.equals(user.getRole(), null)) {
                    redirectURL = "http://localhost:3000/complete-profile";
                    sessionId = sessionDAO.createSession(user.getId(), user.getEmail());
                }else {
                    if (Objects.equals(user.getRole(), "OWNER")) {
                        redirectURL = "http://localhost:3000/driver";
                    }else{
                        redirectURL = "http://localhost:3000/customer";
                    }
                    sessionId = sessionDAO.createSession(user.getId(), user.getEmail(), user.getRole());
                }
            }

            Cookie authCookie = new Cookie("ROAM_SESSION", sessionId);
            authCookie.setHttpOnly(true);
            authCookie.setPath("/");
            authCookie.setMaxAge(6 * 60 * 60); // Strict 6 hours
            resp.addCookie(authCookie);

            resp.addHeader("Set-Cookie", "ROAM_SESSION=" + sessionId + "; Path=/; Max-Age=21600; HttpOnly; SameSite=Lax");
            resp.sendRedirect(redirectURL);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("http://localhost:3000/login?error=server_error");
        }
    }
}