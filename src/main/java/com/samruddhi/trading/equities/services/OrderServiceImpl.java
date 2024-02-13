package com.samruddhi.trading.equities.services;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.samruddhi.trading.equities.domain.Order;
import com.samruddhi.trading.equities.services.base.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String ORDER_URL = "https://api.tradestation.com/v3/orderexecution/orders";
    private final String apiKey;

    public OrderServiceImpl() {
        apiKey = TradeStationAuthImpl.getInstance().getAccessToken().get();
    }

    String payload = "{"
            + "\"AccountID\": \"%s\","
            + "\"Symbol\": \"%s\","
            + "\"Quantity\": \"%s\","
            + "\"LimitPrice\": \"%s\","
            + "\"OrderType\": \"Limit\","
            + "\"TradeAction\": \"BUY\","
            + "\"TimeInForce\": {\"Duration\": \"DAY\"},"
            + "\"Route\": \"Intelligent\""
            + "}";

    public void placeOrder(Order order) throws Exception {
        try {
            //setup Payload
            String updatedPayload = String.format(payload, order.getAccountID(), order.getSymbol(), order.getQuantity(), order.getLimitPrice());
            URL url = new URL(ORDER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = updatedPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                    response.append("\n");
                }
                logger.info("Response from server: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
