package com.samruddhi.trading.equities.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.samruddhi.trading.equities.domain.Order;
import com.samruddhi.trading.equities.services.base.OrderService;

public class OrderServiceImpl implements OrderService {
    public void placeOrder(Order order) {

        String urlString = "https://api.tradestation.com/v3/orderexecution/orders";
        String payload = "{"
                + "\"AccountID\": \"123456782\","
                + "\"Symbol\": \"MSFT\","
                + "\"Quantity\": \"10\","
                + "\"OrderType\": \"Market\","
                + "\"TradeAction\": \"BUY\","
                + "\"TimeInForce\": {\"Duration\": \"DAY\"},"
                + "\"Route\": \"Intelligent\""
                + "}";
        String token = "TOKEN"; // Replace TOKEN with your actual token

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                    response.append("\n");
                }
                System.out.println("Response from server: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
