package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final CSVFormat CSV_FORMAT = CSVFormat.Builder.create(CSVFormat.RFC4180)
        .setHeader()
        .setSkipHeaderRecord(true)
        .setAllowDuplicateHeaderNames(false)
        .build();


        //Path:
    //static final private String PATH_TO_FILE = "C:\\tmp\\redata\\nsw_property_data.csv";

    static final private String PATH_TO_FILE = "/Users/aspentabar/Desktop/Software Engineering/Class Exercise 1/REDataLoader/nsw_property_data.csv";
    public static void main(String[] args) throws Exception {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        // Path of CSV file to read
        final Path csvFilePath = Paths.get(PATH_TO_FILE);


        //!!
        //Connection connection= null;

        String jdbcUrl = "jdbc:mysql://localhost:3306/realestate"; // or whatever your DB name is
        String user = "root"; // or your MySQL username
        String password = "123"; // replace with your actual password

        try (
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        //SVParser parser = CSVParser.parse(csvFilePath, StandardCharsets.UTF_8, CSV_FORMAT)
    ) {

        // Path of the temporary file to write work in progress CSV results to
        Path tempFile = null;
        try (CSVParser parser = CSVParser.parse(csvFilePath, StandardCharsets.UTF_8, CSV_FORMAT)){


  

            System.out.println("File opened");
            String headers = parser.getHeaderNames().toString();
            System.out.println("headers: " + headers);
            // Iterate over input CSV records
            int count = 0;

            //!!
            // String sql = "INSERT INTO property (property_id, address, purchase_price) VALUES (?, ?, ?)";
            // PreparedStatement stmt = connection.prepareStatement(sql);

            String sql = "INSERT INTO property (property_id, address, purchase_price, post_code, download_date, council_name) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);


            for (final CSVRecord record : parser)
            {
                // Get all of the header names and associated values from the record
                final Map<String, String> recordValues = record.toMap();

                // Write the updated values to the output CSV
                System.out.println(recordValues.toString());

                // stmt.setInt(1, Integer.parseInt(record.get("property_id")));
                // stmt.setString(2, record.get("address"));
                // stmt.setBigDecimal(3, new java.math.BigDecimal(record.get("purchase_price")));
                // stmt.executeUpdate();

                System.out.println( "HELLO" + record.get("property_id"));

                stmt.setString(1, record.get("property_id"));
                
                String propertyId = record.get("property_id").isEmpty() ? "999999" : record.get("property_id");
            
                // stmt.setString(2, record.get("address"));
                // stmt.setString(3, record.get("purchase_price"));


                stmt.setString(1, record.get("property_id"));
                stmt.setString(2, record.get("address"));
                stmt.setString(3, record.get("purchase_price"));
                stmt.setString(4, record.get("post_code")); // Assuming 'post_code' is one of the columns in your CSV
                stmt.setString(5, record.get("download_date")); // Assuming 'download_date' is another column in your CSV
                stmt.setString(6, record.get("council_name")); // Assuming 'council_name' is another column in your CSV

                
                stmt.executeUpdate();


    
                count++;

                System.out.println (count);


            }
            System.out.println("Total records: " + count);
        } catch (IOException e) {
            System.out.println("File open failed ");
        }


    }
}
}
