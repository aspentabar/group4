package property;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import sales.HomeSale;


import java.sql.Connection;

public class PropertyDAO {

    // List to hold test data
    private List<HomeSale> sales = new ArrayList<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/realestate"; //MAKE SURE THIS IS YOUR PORT
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";

    public PropertyDAO() {}

    public boolean newProperty(HomeSale homeSale) {
        sales.add(homeSale);
        return true;
    }

    // returns Optional wrapping a HomeSale if id is found, empty Optional otherwise
    public Optional<HomeSale> getPropertyById(String saleID) {
        String selectQuery = "SELECT * FROM property WHERE property_id = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, saleID);

                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        HomeSale sale = new HomeSale();
                        setFields(sale, rs);
                        return Optional.of(sale);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    // returns a List of home sales in a given postCode
    public List<HomeSale> getSalesByPostCode(String postCode) {
        List<HomeSale> salesList = new ArrayList<>();

        String query = "SELECT * FROM property WHERE post_code = ? LIMIT 20";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Retrieve properties
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, postCode);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        HomeSale sale = new HomeSale();
                        setFields(sale, rs); // populate fields from ResultSet
                        salesList.add(sale);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesList;
    }





    // returns all home sales. Potentially large
    public List<HomeSale> getAllSales() {

        List<HomeSale> salesList = new ArrayList<>();

        String query = "SELECT * FROM property LIMIT 20";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HomeSale sale = new HomeSale();
                setFields(sale, rs);

                salesList.add(sale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesList;
    }

    //returns 20 houses closest to and under the upper budget given
    public List<HomeSale> getUnderBudget(String upperBudget) {

        List<HomeSale> salesList = new ArrayList<>();

        // Query to fetch houses under the budget, ordered by price (ascending)
        String query = "SELECT * FROM property WHERE purchase_price <= ? ORDER BY purchase_price DESC LIMIT 20";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Pass the upperBudget into the query
            stmt.setString(1, upperBudget);  // 1 refers to the first "?" placeholder in the query

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HomeSale sale = new HomeSale();
                    setFields(sale, rs);
                    salesList.add(sale);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesList;
    }

//returns the average price of properties within a given postcode

    public Double getAveragePriceInPostcode(String postcode) {

        Double averagePrice = null;

        // Query to calculate the average purchase price for properties in the given postcode
        String query = "SELECT ROUND(AVG(purchase_price),2) AS average_price FROM property WHERE post_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the postcode parameter for the query
            stmt.setString(1, postcode);  // 1 refers to the first "?" placeholder in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    averagePrice = rs.getDouble("average_price");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return averagePrice;  // Returns null if no result is found
    }

    /**
     * Utility method to populate a HomeSale object from a database ResultSet.
     *
     * @param sale the HomeSale object to populate
     * @param rs   the ResultSet from the executed query
     * @throws SQLException if an error occurs accessing result set data
     */
    private static void setFields(HomeSale sale, ResultSet rs) throws SQLException {
        sale.setPropertyId(rs.getString("property_id"));
        sale.setPurchasePrice(rs.getString("purchase_price"));
        sale.setPostCode(rs.getString("post_code"));
        sale.setDownloadDate(rs.getString("download_date"));
        sale.setCouncilName(rs.getString("council_name"));
        sale.setAddress(rs.getString("address"));
        sale.setNatureOfProperty(rs.getString("nature_of_property"));
        sale.setStrataLotNumber(String.valueOf(rs.getInt("strata_lot_number")));
        sale.setPropertyName(rs.getString("property_name"));
        sale.setAreaType(rs.getString("area_type"));
        sale.setContractDate(rs.getString("contract_date"));
        sale.setSettlementDate(rs.getString("settlement_date"));
        sale.setZoning(rs.getString("zoning"));
        sale.setNatureOfProperty(rs.getString("nature_of_property")); // duplicated, but retained for consistency
        sale.setPrimaryPurpose(rs.getString("primary_purpose"));
        sale.setLegalDescription(rs.getString("legal_description"));
        sale.setPropertyType(rs.getString("property_type"));
        sale.setCount(rs.getInt("view_count"));

        String postCode = rs.getString("post_code");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT view_count_postcode FROM postcode WHERE post_code = ?")) {

            stmt.setString(1, postCode);

            try (ResultSet viewRs = stmt.executeQuery()) {
                if (viewRs.next()) {
                    sale.setPostCodeCount(viewRs.getInt("view_count_postcode"));
                }
            }

        }

    }
}