package dao;

import config.MyDB;
import model.TripRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TripRequestDAO {

    // Operation: Insert a new travel request raised by a customer
    public boolean createTripRequest(TripRequest request) {
        String sql = "INSERT INTO trip_requests (customer_id, pickup_address, pickup_city, pickup_state, " +
                "destination_address, destination_city, destination_state, is_round_trip, passenger_count, travel_datetime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, request.getCustomerId());
            stmt.setString(2, request.getPickupAddress());
            stmt.setString(3, request.getPickupCity());
            stmt.setString(4, request.getPickupState());
            stmt.setString(5, request.getDestinationAddress());
            stmt.setString(6, request.getDestinationCity());
            stmt.setString(7, request.getDestinationState());
            stmt.setBoolean(8, request.isRoundTrip());
            stmt.setInt(9, request.getPassengerCount());
            stmt.setTimestamp(10, request.getTravelDatetime());

            System.out.println(request.getTravelDatetime());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<TripRequest> getMatchedTripsForDriver(Long driverId) {
        java.util.List<TripRequest> matchedTrips = new java.util.ArrayList<>();

        // SQL Logic: Find pending trips matching the driver's vehicle location & seating capacity
        String sql = "SELECT DISTINCT tr.* FROM trip_requests tr " +
                "JOIN cars v ON v.owner_id = ? " +
                "JOIN users u ON u.id = ? " +
                "WHERE tr.status = 'PENDING' " +
                "AND v.is_active = true " +
                "AND tr.passenger_count <= v.seating_capacity " +
                "AND (tr.pickup_city = u.city OR tr.pickup_state = u.state)";

        try (Connection conn = config.MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, driverId);
            stmt.setLong(2, driverId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TripRequest trip = new TripRequest();
                    trip.setId(rs.getLong("id"));
                    trip.setCustomerId(rs.getLong("customer_id"));
                    trip.setPickupAddress(rs.getString("pickup_address"));
                    trip.setPickupCity(rs.getString("pickup_city"));
                    trip.setPickupState(rs.getString("pickup_state"));
                    trip.setDestinationAddress(rs.getString("destination_address"));
                    trip.setDestinationCity(rs.getString("destination_city"));
                    trip.setDestinationState(rs.getString("destination_state"));
                    trip.setRoundTrip(rs.getBoolean("is_round_trip"));
                    trip.setPassengerCount(rs.getInt("passenger_count"));
                    trip.setTravelDatetime(rs.getTimestamp("travel_datetime"));
                    trip.setStatus(rs.getString("status"));
                    trip.setCreatedAt(rs.getTimestamp("created_at"));

                    matchedTrips.add(trip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchedTrips;
    }
}
