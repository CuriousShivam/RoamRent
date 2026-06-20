package servlet;

import com.google.gson.Gson;
import config.ResponseUtil;
import dao.TripRequestDAO;
import model.TripRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/trip/*")
public class TripRequestServlet extends HttpServlet {

    private final TripRequestDAO tripDAO = new TripRequestDAO();
    private final Gson gson = new com.google.gson.GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // Matches the space format exactly
            .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || !pathInfo.equals("/create")) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint path not found", null);
            return;
        }

        // Read the incoming JSON body string
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Map the JSON to our Java TripRequest model
        TripRequest newRequest = gson.fromJson(jsonBuffer.toString(), TripRequest.class);

        // Validation against database checks and constraints
        if (newRequest.getCustomerId() == null ||
                newRequest.getPickupAddress() == null || newRequest.getPickupCity() == null || newRequest.getPickupState() == null ||
                newRequest.getDestinationAddress() == null || newRequest.getDestinationCity() == null || newRequest.getDestinationState() == null ||
                newRequest.getTravelDatetime() == null) {

            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required booking details fields", null);
            return;
        }

        // Enforce the passenger count constraint (> 0)
        if (newRequest.getPassengerCount() <= 0) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Passenger count must be greater than 0", null);
            return;
        }

        // Save the request profile into PostgreSQL via JDBC
        boolean isSaved = tripDAO.createTripRequest(newRequest);

        if (isSaved) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_CREATED, "Trip request raised successfully!", null);
        } else {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database trip creation failed", null);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
