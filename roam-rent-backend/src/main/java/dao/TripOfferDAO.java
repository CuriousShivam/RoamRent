package dao;

import config.MyDB;
import model.TripOffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TripOfferDAO {

    // Helper: Check if the trip status is 'PENDING'
    private boolean isTripPending(Connection conn, Long requestId) throws SQLException {
        String sql = "SELECT status FROM trip_requests WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String currentStatus = rs.getString("status");
                    return "PENDING".equalsIgnoreCase(currentStatus);
                }
            }
        }
        return false;
    }

    // Operation: Submit a driver price offer
    public String submitOffer(TripOffer offer) {
        String insertSql = "INSERT INTO trip_offers (request_id, driver_id, car_id, offered_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyDB.getConnection()) {

            // 1. Enforce business rule check
            if (!isTripPending(conn, offer.getRequestId())) {
                return "NOT_PENDING"; // Trip is already booked, expired, or cancelled
            }

            // 2. Insert the offer
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setLong(1, offer.getRequestId());
                stmt.setLong(2, offer.getDriverId());
                stmt.setLong(3, offer.getCarId());
                stmt.setBigDecimal(4, offer.getOfferedPrice());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0 ? "SUCCESS" : "FAILED";
            }

        } catch (SQLException e) {
            // Check for PostgreSQL unique index constraint violation code (23505)
            // This prevents the same driver from submitting multiple bids to the same trip request
            if ("23505".equals(e.getSQLState())) {
                return "DUPLICATE_OFFER";
            }
            e.printStackTrace();
            return "ERROR";
        }
    }

    public java.util.List<model.OfferResponse> getOffersForCustomerRequest(Long requestId) {
        java.util.List<model.OfferResponse> offersList = new java.util.ArrayList<>();

        // SQL Logic: Combine bid pricing with driver data and car specifications
        String sql = "SELECT o.id AS offer_id, o.request_id, o.offered_price, o.status AS offer_status, o.created_at, " +
                "u.id AS driver_id, u.name AS driver_name, u.phone AS driver_phone, " +
                "v.company AS car_company, v.model AS car_model " +
                "FROM trip_offers o " +
                "JOIN users u ON o.driver_id = u.id " +
                "JOIN cars v ON o.car_id = v.id " +
                "WHERE o.request_id = ? " +
                "ORDER BY o.offered_price ASC"; // Shows the lowest price bids first!

        try (Connection conn = config.MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    model.OfferResponse offer = new model.OfferResponse();
                    offer.setOfferId(rs.getLong("offer_id"));
                    offer.setRequestId(rs.getLong("request_id"));
                    offer.setOfferedPrice(rs.getBigDecimal("offered_price"));
                    offer.setOfferStatus(rs.getString("offer_status"));
                    offer.setCreatedAt(rs.getTimestamp("created_at"));

                    offer.setDriverId(rs.getLong("driver_id"));
                    offer.setDriverName(rs.getString("driver_name"));
                    offer.setDriverPhone(rs.getString("driver_phone"));

                    offer.setCarCompany(rs.getString("car_company"));
                    offer.setCarModel(rs.getString("car_model"));

                    offersList.add(offer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offersList;
    }
}
