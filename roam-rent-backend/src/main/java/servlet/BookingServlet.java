package servlet;

import com.google.gson.Gson;
import config.ResponseUtil;
import dao.BookingDAO;
import model.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/booking/*")
public class BookingServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || !pathInfo.equals("/accept")) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint path not found", null);
            return;
        }

        // Read incoming browser JSON payload strings
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Map data to the model
        Booking incomingSelection = gson.fromJson(jsonBuffer.toString(), Booking.class);

        if (incomingSelection.getRequestId() == null || incomingSelection.getOfferId() == null) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required booking reference parameters", null);
            return;
        }

        // Process transaction lifecycle path rules
        String result = bookingDAO.acceptOfferAndCreateBooking(incomingSelection);

        switch (result) {
            case "SUCCESS":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_CREATED, "Trip successfully booked and scheduled!", null);
                break;
            case "TRIP_NOT_AVAILABLE":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_GONE, "This ride request is no longer open for booking.", null);
                break;
            case "ALREADY_BOOKED":
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_CONFLICT, "This trip or offer has already been confirmed.", null);
                break;
            default:
                ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database failed to finalize booking.", null);
                break;
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
