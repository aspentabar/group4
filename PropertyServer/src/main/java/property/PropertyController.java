package property;

import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

public class PropertyController {

    private PropertyDAO properties;

    public PropertyController(PropertyDAO properties) {
        this.properties = properties;
    }

    public void createProperty(Context ctx) {
        HomeSale property = ctx.bodyValidator(HomeSale.class).get();

        if (properties.newProperty(property)) {
            ctx.result("Property Created");
            ctx.status(201);
        } else {
            ctx.result("Failed to add property");
            ctx.status(400);
        }
    }

    // implements Get /sales
    public void getAllProperties(Context ctx) {
        List<HomeSale> allProperties = properties.getAllProperties();
        if (allProperties.isEmpty()) {
            ctx.result("No Properties Found");
            ctx.status(404);
        } else {
            ctx.json(allProperties);
            ctx.status(200);
        }
    }

    // implements GET /properties/sales/{saleID}
    public void getPropertyByID(Context ctx, String id) {
        Optional<HomeSale> property = properties.getPropertyById(id);
        property.map(ctx::json)
                .orElseGet(() -> error(ctx, "Property not found", 404));
    }

    // Implements GET /properties/sales/postcode/{postcodeID}
    public void findPropertyByPostCode(Context ctx, String postCode) {
        List<HomeSale> propertiesList = properties.getPropertiesByPostCode(postCode);
        if (propertiesList.isEmpty()) {
            ctx.result("No properties for postcode found");
            ctx.status(404);
        } else {
            ctx.json(propertiesList);
            ctx.status(200);
        }
    }

    public void findPropertiesUnderBudget(Context ctx, String budget) {
        List<HomeSale> propertiesList = properties.getPropertiesUnderBudget(budget);
        if (propertiesList.isEmpty()) {
            ctx.result("No properties found");
            ctx.status(404);
        } else {
            ctx.json(propertiesList);
            ctx.status(200);
        }
    }

    public void findAveragePriceInPostcode(Context ctx, String postcode) {
        Double averagePrice = properties.getAveragePriceInPostcode(postcode);
        if (averagePrice == null) {
            ctx.result("Average Not Found");
            ctx.status(404);
        } else {
            ctx.json(averagePrice);
            ctx.status(200);
        }
    }

    private Context error(Context ctx, String msg, int code) {
        ctx.result(msg);
        ctx.status(code);
        return ctx;
    }
}
