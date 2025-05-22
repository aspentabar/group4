import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class ApiGateway {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        // GET by postcode
        server.createContext("/properties/sales/postcode", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length < 5) {
                    exchange.sendResponseHeaders(400, -1); // Bad request
                    return;
                }
                String postCode = parts[parts.length - 1];

                // Forward to PropertyServer
                String propertyResponse = callService("http://localhost:8001/properties/sales/postcode/" + postCode, "GET", null);

                // Trigger Analytics increment
                callService("http://localhost:8080/analytics/postcode/" + postCode + "/increment", "POST", null);

                sendJsonResponse(exchange, 200, propertyResponse);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        // GET by saleID
        server.createContext("/properties/sales", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");

                // Check for specific saleID
                if (parts.length == 4) {
                    String saleId = parts[3];
                    String response = callService("http://localhost:8001/properties/sales/" + saleId, "GET", null);

                    // Trigger Analytics increment
                    callService("http://localhost:8080/analytics/sale/" + saleId + "/increment", "POST", null);

                    sendJsonResponse(exchange, 200, response);
                } else {
                    exchange.sendResponseHeaders(400, -1); // Bad request
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        // GET all properties
        server.createContext("/properties/sales/all", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String response = callService("http://localhost:8001/properties/sales", "GET", null);
                sendJsonResponse(exchange, 200, response);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        // POST to create new property
        server.createContext("/properties/sales/create", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = callService("http://localhost:8001/properties/sales", "POST", requestBody);
                sendJsonResponse(exchange, 201, response);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        });

        server.start();
        System.out.println("API Gateway running at http://localhost:8081/");
    }

    private static String callService(String url, String method, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));

            if ("POST".equalsIgnoreCase(method)) {
                builder.POST(HttpRequest.BodyPublishers.ofString(json != null ? json : ""))
                       .header("Content-Type", "application/json");
            } else {
                builder.GET();
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to reach service at " + url + "\"}";
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, int status, String responseBody) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
