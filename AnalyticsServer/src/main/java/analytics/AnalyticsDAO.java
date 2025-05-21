package analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnalyticsDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/realestate"; // adjust if needed
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";

    public AnalyticsDAO() {
        // Optional: load JDBC driver here if necessary
    }

    // Increment the view count for a sale (property_id)
    public boolean incrementSaleAccessCount(String saleId) {
        String updateQuery = "UPDATE property SET view_count = view_count + 1 WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, saleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // true if a row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Increment the view count for a postcode
    public boolean incrementPostCodeAccessCount(String postCode) {
        String updateQuery = "UPDATE postcode SET view_count_postcode = view_count_postcode + 1 WHERE post_code = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, postCode);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get the view count for a sale (property_id)
    public Optional<Integer> getSaleAccessCount(String saleId) {
        String query = "SELECT view_count FROM property WHERE property_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("view_count"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Get the view count for a postcode
    public Optional<Integer> getPostCodeAccessCount(String postCode) {
        String query = "SELECT view_count_postcode FROM postcode WHERE post_code = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, postCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("view_count_postcode"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Get all sale access counts as a list of Analytics objects
    public List<Analytics> getAllSaleAccessCounts() {
        List<Analytics> counts = new ArrayList<>();
        String query = "SELECT property_id, view_count FROM property";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Analytics analytics = new Analytics();
                analytics.setSaleId(rs.getString("property_id"));
                analytics.setSaleAccessCount(rs.getInt("view_count"));
                counts.add(analytics);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counts;
    }

    // Get all postcode access counts as a list of Analytics objects
    public List<Analytics> getAllPostCodeAccessCounts() {
        List<Analytics> counts = new ArrayList<>();
        String query = "SELECT post_code, view_count_postcode FROM postcode";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Analytics analytics = new Analytics();
                analytics.setPostCode(rs.getString("post_code"));
                analytics.setPostCodeAccessCount(rs.getInt("view_count_postcode"));
                counts.add(analytics);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counts;
    }

}
