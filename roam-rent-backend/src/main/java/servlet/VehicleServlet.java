package servlet;

import com.google.gson.Gson;
import config.ResponseUtil;
import dao.VehicleDAO;
import model.Vehicle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/vehicle/*")
public class VehicleServlet extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || !pathInfo.equals("/register")) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint path not found", null);
            return;
        }

        // Read the JSON body sent from Next.js
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Map the JSON directly to our Java Vehicle model
        Vehicle newVehicle = gson.fromJson(jsonBuffer.toString(), Vehicle.class);

        // Validation checks
        if (newVehicle.getOwnerId() == null || newVehicle.getCompany() == null ||
                newVehicle.getModel() == null || newVehicle.getLicensePlate() == null ||
                newVehicle.getSeatingCapacity() <= 0) {

            ResponseUtil.sendJson(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required vehicle data fields", null);
            return;
        }

        // Check if the license plate is already registered
        if (vehicleDAO.isLicensePlateRegistered(newVehicle.getLicensePlate())) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_CONFLICT, "This license plate number is already registered", null);
            return;
        }

        // Save the vehicle profile into PostgreSQL
        boolean isSaved = vehicleDAO.registerVehicle(newVehicle);

        if (isSaved) {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_CREATED, "Vehicle registered successfully!", null);
        } else {
            ResponseUtil.sendJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database vehicle registration failed", null);
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