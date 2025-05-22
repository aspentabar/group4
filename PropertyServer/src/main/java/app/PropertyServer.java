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

        // Create app
        var app = Javalin.create()
                .start(8001);

        // Health check route
        app.get("/", ctx -> ctx.result("Property server is running"));

        // ✅ Register all routes directly on app
        app.get("/properties/sales/{saleID}", ctx ->
                propertyHandler.getPropertyByID(ctx, ctx.pathParam("saleID")));

        app.get("/properties/sales", ctx ->
                propertyHandler.getAllProperties(ctx));

        app.post("/properties/sales", ctx ->
                propertyHandler.createProperty(ctx));

        app.get("/properties/sales/postcode/{postcode}", ctx ->
                propertyHandler.findPropertyByPostCode(ctx, ctx.pathParam("postcode")));

        app.get("/properties/sales/underBudget/{budget}", ctx ->
                propertyHandler.findPropertiesUnderBudget(ctx, ctx.pathParam("budget")));

        app.get("/properties/sales/avgPrice/{postcode}", ctx ->
                propertyHandler.findAveragePriceInPostcode(ctx, ctx.pathParam("postcode")));
    }
}