package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ResponseUtil;
import dao.TripRequestDAO;
import model.TripRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/driver/dashboard")
public class DriverDashboardServlet extends HttpServlet {

    private final TripRequestDAO tripDAO = new TripRequestDAO();

    // Configure GSON to keep our clean date format intact for the frontend
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String driverIdParam = req.getParameter("driverId");

        // 1. Validate parameter input
        if (driverIdParam == null || driverIdParam.trim().isEmpty()) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required driverId parameter", null);
            return;
        }

        try {
            Long driverId = Long.parseLong(driverIdParam);

            // 2. Fetch matched locations data items from the database
            List<TripRequest> dashboardFeed = tripDAO.getMatchedTripsForDriver(driverId);

            // 3. Set custom CORS response settings headers manual lines
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

            // 4. Return serialized array stream
            String jsonOutput = this.gson.toJson(dashboardFeed);
            resp.getWriter().write(jsonOutput);

        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid driverId number format structure", null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve dashboard feeds", null);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
