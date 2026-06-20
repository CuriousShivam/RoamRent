package dao;

import config.MyDB;
import model.Vehicle;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAO {

    // Operation 1: Insert a new vehicle linked to a driver
    public boolean registerVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO cars (owner_id, company, model, license_plate, seating_capacity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, vehicle.getOwnerId());
            stmt.setString(2, vehicle.getCompany());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getLicensePlate());
            stmt.setInt(5, vehicle.getSeatingCapacity());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Operation 2: Check if a license plate already exists
    public boolean isLicensePlateRegistered(String licensePlate) {
        String sql = "SELECT 1 FROM cars WHERE license_plate = ?";

        try (Connection conn = MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, licensePlate);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}