package sales;

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

public class SalesDAO {

    // List to hold test data
    private List<HomeSale> sales = new ArrayList<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3308/realestate"; //MAKE SURE THIS IS YOUR PORT
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";

    public SalesDAO() {
        // Test data
        // sales.add(new HomeSale("0", "2257", "2000000"));
        // sales.add(new HomeSale("1", "2262", "1300000"));
        // sales.add(new HomeSale("2", "2000", "4000000"));
        // sales.add(new HomeSale("3", "2000", "1000000"));
    }

    public boolean newSale(HomeSale homeSale) {
        sales.add(homeSale);
        return true;
    }

    // returns Optional wrapping a HomeSale if id is found, empty Optional otherwise
    public Optional<HomeSale> getSaleById(String saleID) {
        String query = "SELECT * FROM property WHERE property_id = ? LIMIT 1";
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, saleID);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                HomeSale sale = new HomeSale();
                sale.setPropertyId(rs.getString("property_id"));
                sale.setPurchasePrice(rs.getString("purchase_price"));
                sale.setPostCode(rs.getString("post_code"));
                
                // Return the found HomeSale wrapped in an Optional
                return Optional.of(sale);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // If no result found, return an empty Optional
        return Optional.empty();
    }

    // returns a List of home sales in a given postCode
    public List<HomeSale> getSalesByPostCode(String postCode) {
        List<HomeSale> salesList = new ArrayList<>();

        String query = "SELECT * FROM property WHERE post_code = ? LIMIT 20";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, postCode);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HomeSale sale = new HomeSale();
                    sale.setPropertyId(rs.getString("property_id"));
                    sale.setPurchasePrice(rs.getString("purchase_price"));
                    sale.setPostCode(rs.getString("post_code"));

                    salesList.add(sale);
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
            sale.setPropertyId(rs.getString("property_id"));
            sale.setPurchasePrice(rs.getString("purchase_price"));
            sale.setPostCode(rs.getString("post_code"));

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
                sale.setPropertyId(rs.getString("property_id"));
                sale.setPurchasePrice(rs.getString("purchase_price"));
                sale.setPostCode(rs.getString("post_code"));
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

}

