package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * Main class for loading NSW real estate data from a CSV file into a MySQL database.
 * Parses the CSV, extracts fields, and inserts them into the `property` table.
 */
public class Main {

    /**
     * CSV format configuration for parsing the NSW data file.
     * Assumes header row exists, skips duplicate headers.
     */
    private static final CSVFormat CSV_FORMAT = CSVFormat.Builder.create(CSVFormat.RFC4180)
        .setHeader()
        .setSkipHeaderRecord(true)
        .setAllowDuplicateHeaderNames(false)
        .build();

    /** Absolute path to the NSW real estate CSV file */
    static final private String PATH_TO_FILE =
        "/Users/abbystevenson/Desktop/Northeastern/Second Year Classes/DOC/group4/REDataLoader/nsw_property_data.csv";

    /**
     * Main method that performs the CSV parsing and inserts data into the MySQL database.
     *
     * @param args Command line arguments (not used)
     * @throws Exception if connection or file operations fail
     */
    public static void main(String[] args) throws Exception {

        System.out.println("Hello and welcome!");

        // Path of CSV file to read
        final Path csvFilePath = Paths.get(PATH_TO_FILE);

        final String jdbcUrl = "jdbc:mysql://localhost:3308/realestate";
        final String user = "root"; // or your MySQL username
        final String password = "123"; // replace with your actual password

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
            try (CSVParser parser = CSVParser.parse(csvFilePath,
                StandardCharsets.UTF_8, CSV_FORMAT)){

                System.out.println("File opened");
                final String headers = parser.getHeaderNames().toString();
                System.out.println("headers: " + headers);
                // Iterate over input CSV records
                int count = 0;

                final String sql = "INSERT INTO property (property_id, " +
                    "address, " +
                    "purchase_price, " +
                    "post_code, " +
                    "download_date, " +
                    "council_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";


                final PreparedStatement stmt = connection.prepareStatement(sql);

                // Prepared statements for postcode existence check and insertion
                final String checkPostcodeSql = "SELECT 1 FROM postcode WHERE post_code = ?";
                final PreparedStatement checkPostcodeStmt = connection.prepareStatement(checkPostcodeSql);

                final String insertPostcodeSql = "INSERT INTO postcode (post_code, view_count_postcode) VALUES (?, 0)";
                final PreparedStatement insertPostcodeStmt = connection.prepareStatement(insertPostcodeSql);

                for (final CSVRecord record : parser) {
                    // Get all of the header names and associated values from the record
                    final Map<String, String> recordValues = record.toMap();

                    // Write the updated values to the output CSV
                    System.out.println(recordValues.toString());


                    String propertyId = record.get("property_id");
                    String address = record.get("address");
                    String purchasePrice = record.get("purchase_price");
                    String postCode = record.get("post_code");
                    String downloadDate = record.get("download_date");
                    String councilName = record.get("council_name");

                    // Insert into postcode table if not already present
                    if (postCode != null && !postCode.isBlank()) {
                        checkPostcodeStmt.setString(1, postCode);
                        ResultSet rs = checkPostcodeStmt.executeQuery();
                        if (!rs.next()) {
                            insertPostcodeStmt.setString(1, postCode);
                            insertPostcodeStmt.executeUpdate();
                        }
                        rs.close();
                    }

                    stmt.setString(1, record.get("property_id"));
                    stmt.setString(2, record.get("address"));
                    stmt.setString(3, record.get("purchase_price"));
                    stmt.setString(4, record.get("post_code"));
                    stmt.setString(5, record.get("download_date"));
                    stmt.setString(6, record.get("council_name"));

                    stmt.executeUpdate();

                    count++;

                    System.out.println (count);
                }
                System.out.println("Total records: " + count);
                stmt.close();
                checkPostcodeStmt.close();
                insertPostcodeStmt.close();
            } 

            catch (IOException e) {
                System.out.println("File open failed ");
            }
        }
    }
}
