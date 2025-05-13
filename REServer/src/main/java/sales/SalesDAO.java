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

    private static final String DB_URL = "jdbc:mysql://localhost:3306/realestate";
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
        for (HomeSale u : sales) {
            if (u.getPropertyId().equals(saleID)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    // returns a List of home sales in a given postCode
    public List<HomeSale> getSalesByPostCode(String postCode) {
        List<HomeSale> salesList = new ArrayList<>();
        for (HomeSale sale : sales) {
            if (sale.getPostCode().equals(postCode)) {
                salesList.add(sale);
            }
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
    


}
