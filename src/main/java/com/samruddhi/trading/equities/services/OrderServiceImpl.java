package com.samruddhi.trading.equities.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.services.base.OrderService;
import common.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public PlaceOrderResponse placeOrder(PlaceOrderPayload order) throws Exception {

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

            StringBuilder response = new StringBuilder();
            try (Scanner scanner = new Scanner(connection.getInputStream())) {

                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                    response.append("\n");
                }
                logger.info("Response from server: " + response.toString());
            }
            return JsonParser.getPlaceOrderResponse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
