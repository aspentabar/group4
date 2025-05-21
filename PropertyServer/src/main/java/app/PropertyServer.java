package app;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import property.PropertyDAO;
import property.PropertyController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PropertyServer {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyServer.class);

    public static void main(String[] args) {

        // in memory test data store
        var properties = new PropertyDAO();

        // API implementation
        PropertyController propertyHandler = new PropertyController(properties);

        // start Javalin on port 8001
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Real Estate server is running"))
                .start(8001);

        // configure endpoint handlers to process HTTP requests
        JavalinConfig config = new JavalinConfig();
        config.router.apiBuilder(() -> {
            // Sales records are immutable hence no PUT and DELETE

            // return a sale by sale ID
            app.get("/properties/sales/{saleID}", ctx -> {
                propertyHandler.getPropertyByID(ctx, ctx.pathParam("saleID"));
            });
            // get all sales records - could be big!
            app.get("/properties/sales", ctx -> {
                propertyHandler.getAllProperties(ctx);
            });
            // create a new sales record
            app.post("/properties/sales", ctx -> {
                propertyHandler.createProperty(ctx);
            });
            // Get all sales for a specified postcode
            app.get("/properties/sales/postcode/{postcode}", ctx -> {
                propertyHandler.findPropertyByPostCode(ctx, ctx.pathParam("postcode"));
            });
            //Get 20 closest houses under budget
            app.get("/properties/sales/underBudget/{budget}", ctx -> {
                propertyHandler.findPropertiesUnderBudget(ctx, ctx.pathParam("budget"));
            });
            //Gets the average price of properties in the given postcode
            app.get("/properties/sales/avgPrice/{postcode}", ctx -> {
                propertyHandler.findAveragePriceInPostcode(ctx, ctx.pathParam("postcode"));
            });
        });


    }
}


