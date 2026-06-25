package dao;

import config.MyDB;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Operation 1: Register a new user into PostgreSQL
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password, phone, role, address, city, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println(user);
        try (Connection conn = MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            System.out.println("Inside regester user");
            boolean isAlive = conn.isValid(3);

            System.out.println("Is database connection alive? " + isAlive);

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // Note: In production, hash this password!
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getCity());
            stmt.setString(8, user.getState());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Operation 2: Find a user by email (Critical for login validation)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setAddress(rs.getString("address"));
                    user.setCity(rs.getString("city"));
                    user.setState(rs.getString("state"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user account is not found
    }

    public User getUserByOAuth(String provider, String oauthId) {
        String sql = "SELECT * FROM users WHERE oauth_provider = ? AND oauth_id = ?";
        try (Connection conn = config.MyDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "GOOGLE");
            stmt.setString(2, oauthId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setRole(rs.getString("role"));
                    user.setAddress(rs.getString("address"));
                    user.setCity(rs.getString("city"));
                    user.setState(rs.getString("state"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
