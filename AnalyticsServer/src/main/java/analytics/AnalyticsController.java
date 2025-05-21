package analytics;

import io.javalin.http.Context;

import java.util.List;
import java.util.Optional;

public class AnalyticsController {

    private AnalyticsDAO analyticsDAO;

    public AnalyticsController(AnalyticsDAO analyticsDAO) {
        this.analyticsDAO = analyticsDAO;
    }

    // POST /analytics/sale/{saleID}/increment
    public void incrementSaleAccessCount(Context ctx, String saleId) {
        boolean success = analyticsDAO.incrementSaleAccessCount(saleId);
        if (success) {
            ctx.result("Sale access count incremented");
            ctx.status(200);
        } else {
            ctx.result("Sale ID not found");
            ctx.status(404);
        }
    }

    // POST /analytics/postcode/{postcode}/increment
    public void incrementPostcodeAccessCount(Context ctx, String postCode) {
        boolean success = analyticsDAO.incrementPostCodeAccessCount(postCode);
        if (success) {
            ctx.result("Postcode access count incremented");
            ctx.status(200);
        } else {
            ctx.result("Postcode not found");
            ctx.status(404);
        }
    }

    // GET /analytics/sale/{saleID}
    public void getSaleAccessCount(Context ctx, String saleId) {
        Optional<Integer> count = analyticsDAO.getSaleAccessCount(saleId);
        if (count.isPresent()) {
            ctx.json(count.get());
            ctx.status(200);
        } else {
            ctx.result("Sale ID not found");
            ctx.status(404);
        }
    }

    // GET /analytics/postcode/{postcode}
    public void getPostcodeAccessCount(Context ctx, String postCode) {
        Optional<Integer> count = analyticsDAO.getPostCodeAccessCount(postCode);
        if (count.isPresent()) {
            ctx.json(count.get());
            ctx.status(200);
        } else {
            ctx.result("Postcode not found");
            ctx.status(404);
        }
    }

    // GET /analytics/sales - get all sale access counts
    public void getAllSaleAccessCounts(Context ctx) {
        List<Analytics> allSales = analyticsDAO.getAllSaleAccessCounts();
        if (allSales.isEmpty()) {
            ctx.result("No sale access counts found");
            ctx.status(404);
        } else {
            ctx.json(allSales);
            ctx.status(200);
        }
    }

    // GET /analytics/postcodes - get all postcode access counts
    public void getAllPostcodeAccessCounts(Context ctx) {
        List<Analytics> allPostcodes = analyticsDAO.getAllPostCodeAccessCounts();
        if (allPostcodes.isEmpty()) {
            ctx.result("No postcode access counts found");
            ctx.status(404);
        } else {
            ctx.json(allPostcodes);
            ctx.status(200);
        }
    }

}
