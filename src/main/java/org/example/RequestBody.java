package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestBody implements HttpHandler {
    private final ObjectHandler objectHandler;

    public RequestBody(UserHandler userHandler) {
        objectHandler = new UserHandler();
    }

    public RequestBody(LoginHandler loginHandler) {
        objectHandler = new LoginHandler();
    }

    public RequestBody(DataUserHandler dataUserHadler) {
        objectHandler = new DataUserHandler();
    }

    public RequestBody(ComponentHandler componentHandler) {
        objectHandler = new ComponentHandler();
    }

    public RequestBody(GetComponentsHandler getComponentsHandler) {
        objectHandler = new GetComponentsHandler();
    }

    public RequestBody(GetComponentByIdHandler getComponentsHandler) {
        objectHandler = new GetComponentByIdHandler();
    }

    public RequestBody(GetComponentsByIdHandler getComponentsHandler) {
        objectHandler = new GetComponentsByIdHandler();
    }

    public RequestBody(PriceHandler priceHandler) {
        objectHandler = new PriceHandler();
    }

    public RequestBody(HistoryHandler historyHandler) {
        objectHandler = new HistoryHandler();
    }

    public RequestBody(GetHistoryHandler getHistoryHandler) {
        objectHandler = new GetHistoryHandler();
    }

    public RequestBody(BalanceHandler balanceHandler) {
        objectHandler = new BalanceHandler();
    }

    public RequestBody(GetBalanceHandler getBalanceHandler) {
        objectHandler = new GetBalanceHandler();
    }

    public RequestBody(DeleteComponentHandler deleteComponentHandler) {
        objectHandler = new DeleteComponentHandler();
    }

    public RequestBody(AttendenceHandler attendenceHandler) {
        objectHandler = new AttendenceHandler();
    }

    public RequestBody(GetMostPopularComponents getMostPopularComponents) {
        objectHandler = new GetMostPopularComponents();
    }

    public RequestBody(GetComponentsByIdUserHandler getComponentsByIdUserHandler) {
        objectHandler = new GetComponentsByIdUserHandler();
    }

    public RequestBody(GetComponentsByIdAuctionHandler getComponentsByIdAuctionHandler) {
        objectHandler = new GetComponentsByIdAuctionHandler();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            InputStream is = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            String body = requestBody.toString();
            objectHandler.PutData(body);

            DatabaseConnection dbConnection = new DatabaseConnection();
            try {
                if (objectHandler.DatabaseBoolean(dbConnection)) {
                    objectHandler.DatabaseOperation(dbConnection);
                    String response = objectHandler.getPositiveResponse();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, 0);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                else {
                    String response = objectHandler.getNegativeResponse();
                    exchange.sendResponseHeaders(401, 0);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                String response = objectHandler.getNegativeResponse();
                exchange.sendResponseHeaders(500, 0);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }

    }
}
