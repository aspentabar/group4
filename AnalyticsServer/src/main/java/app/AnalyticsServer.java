package app;

import analytics.AnalyticsDAO;
import analytics.AnalyticsController;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyticsServer {
    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsServer.class);

    public static void main(String[] args) {

        // In-memory or database-backed analytics data access object
        AnalyticsDAO analyticsDAO = new AnalyticsDAO();

        // Controller handles HTTP requests and uses DAO
        AnalyticsController analyticsController = new AnalyticsController(analyticsDAO);

        // Create Javalin server, start on port 8080 (different port from property server)
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Analytics server is running"))
                .start(8080);

        JavalinConfig config = new JavalinConfig();
        config.router.apiBuilder(() -> {
            // Increment access count for a sale ID
            app.post("/analytics/sale/{saleID}/increment", ctx -> {
                String saleID = ctx.pathParam("saleID");
                analyticsController.incrementSaleAccessCount(ctx, saleID);
            });

            // Increment access count for a postcode
            app.post("/analytics/postcode/{postcode}/increment", ctx -> {
                String postcode = ctx.pathParam("postcode");
                analyticsController.incrementPostcodeAccessCount(ctx, postcode);
            });

            // Get access count for a sale ID
            app.get("/analytics/sale/{saleID}", ctx -> {
                String saleID = ctx.pathParam("saleID");
                analyticsController.getSaleAccessCount(ctx, saleID);
            });

            // Get access count for a postcode
            app.get("/analytics/postcode/{postcode}", ctx -> {
                String postcode = ctx.pathParam("postcode");
                analyticsController.getPostcodeAccessCount(ctx, postcode);
            });

            // Optionally, list all sale access counts
            app.get("/analytics/sales", ctx -> {
                analyticsController.getAllSaleAccessCounts(ctx);
            });

            // Optionally, list all postcode access counts
            app.get("/analytics/postcodes", ctx -> {
                analyticsController.getAllPostcodeAccessCounts(ctx);
            });
        });

    }
}

