package dao;

import config.MyDB;
import model.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingDAO {

    public String acceptOfferAndCreateBooking(Booking booking) {
        String insertBookingSql = "INSERT INTO bookings (request_id, offer_id) VALUES (?, ?)";
        String updateOfferSql = "UPDATE trip_offers SET status = 'ACCEPTED' WHERE id = ?";
        String updateRequestSql = "UPDATE trip_requests SET status = 'ACCEPTED' WHERE id = ? AND status = 'PENDING'";

        Connection conn = null;
        try {
            conn = MyDB.getConnection();
            // Turn off auto-commit to start a multi-step database transaction
            conn.setAutoCommit(false);

            // Step 1: Lock down and update the Trip Request status (only if it is still PENDING)
            try (PreparedStatement stmtReq = conn.prepareStatement(updateRequestSql)) {
                stmtReq.setLong(1, booking.getRequestId());
                int updatedRows = stmtReq.executeUpdate();

                if (updatedRows == 0) {
                    // This means the trip request was already accepted by another driver or cancelled
                    conn.rollback();
                    return "TRIP_NOT_AVAILABLE";
                }
            }

            // Step 2: Update the winning Driver Offer status to 'ACCEPTED'
            try (PreparedStatement stmtOff = conn.prepareStatement(updateOfferSql)) {
                stmtOff.setLong(1, booking.getOfferId());
                stmtOff.executeUpdate();
            }

            // Step 3: Insert the clean data row into your new bookings table
            try (PreparedStatement stmtBook = conn.prepareStatement(insertBookingSql)) {
                stmtBook.setLong(1, booking.getRequestId());
                stmtBook.setLong(2, booking.getOfferId());
                stmtBook.executeUpdate();
            }

            // If all 3 commands execute without hitting errors, commit them forever
            conn.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            // If any step hits a unique constraint or breaks, cancel all changes instantly
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }

            // Handle PostgreSQL unique constraint error (23505) for double bookings
            if ("23505".equals(e.getSQLState())) {
                return "ALREADY_BOOKED";
            }
            e.printStackTrace();
            return "ERROR";
        }  finally {
            // Restore connection default behavior and close it safely
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}