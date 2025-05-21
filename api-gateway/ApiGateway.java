import com.sun.net.httpserver.*;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class ApiGateway {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // GET forward
        server.createContext("/properties", exchange -> {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String response = callService("http://localhost:8081/properties", "GET", null);
                sendJsonResponse(exchange, 200, response);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = callService("http://localhost:8081/properties", "POST", requestBody);
                sendJsonResponse(exchange, 201, response);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        server.start();
        System.out.println("API Gateway running at http://localhost:8080/");
    }

    private static String callService(String url, String method, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

            if ("POST".equalsIgnoreCase(method)) {
                builder.POST(HttpRequest.BodyPublishers.ofString(json))
                       .header("Content-Type", "application/json");
            } else {
                builder.GET();
            }

            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to reach service\"}";
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, int status, String responseBody) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}