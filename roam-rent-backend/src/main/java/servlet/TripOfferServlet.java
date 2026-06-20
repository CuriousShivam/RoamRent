package servlet;

import com.google.gson.Gson;
import config.ResponseUtil;
import dao.TripOfferDAO;
import model.TripOffer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/offer/*")
public class TripOfferServlet extends HttpServlet {

    private final TripOfferDAO offerDAO = new TripOfferDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || !pathInfo.equals("/submit")) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint path not found", null);
            return;
        }

        // Read JSON data body from Next.js
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Map text strings to TripOffer object model
        TripOffer newOffer = gson.fromJson(jsonBuffer.toString(), TripOffer.class);

        // Input parameter validation parameters
        if (newOffer.getRequestId() == null || newOffer.getDriverId() == null ||
                newOffer.getCarId() == null || newOffer.getOfferedPrice() == null) {

            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required offer profile fields", null);
            return;
        }

        // Enforce database price threshold constraint (> 0)
        if (newOffer.getOfferedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Offered price must be greater than 0.00", null);
            return;
        }

        // Run the submission check logic path
        String result = offerDAO.submitOffer(newOffer);

        switch (result) {
            case "SUCCESS":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_CREATED, "Your pricing offer was successfully submitted!", null);
                break;
            case "NOT_PENDING":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_GONE, "Cannot submit offer. This trip request is no longer active or pending.", null);
                break;
            case "DUPLICATE_OFFER":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_CONFLICT, "You have already submitted a price bid for this trip request.", null);
                break;
            default:
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database failed to process your bid.", null);
                break;
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

