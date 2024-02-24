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
    private String PLACE_ORDER_URL = "https://api.tradestation.com/v3/orderexecution/orders";
    private String CANCEL_ORDER_URL = "https://api.tradestation.com/v3/orderexecution/orders/%s";

    public PlaceOrderResponse placeOrder(PlaceOrderPayload order) throws Exception {


        // TO DO fix me with proper option ticker and Limit order
        /**
         * {
         * "AccountID": "123456782",
         * "Symbol": "MSFT",
         * "Quantity": "10",
         * "OrderType": "Market",
         * "TradeAction": "BUY",
         * "TimeInForce": {
         * "Duration": "DAY"
         * },
         * "Route": "Intelligent"
         * }
         */
        String payload = "{"
                + "\"AccountID\": \"%S\","
                + "\"Symbol\": \"%S\","
                + "\"Quantity\": \"%s\","
                + "\"OrderType\": \"%S\","
                + "\"TradeAction\": \"%S\","
                + "\"TimeInForce\": {\"Duration\": \"DAY\"},"
                + "\"Route\": \"Intelligent\""
                + "}";
        String token = TradeStationAuthImpl.getInstance().getAccessToken().get(); // Replace TOKEN with your actual token

        try {
            URL url = new URL(PLACE_ORDER_URL);
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

    @Override
    public void cancelOrder(String orderId) throws Exception {
        String token = TradeStationAuthImpl.getInstance().getAccessToken().get(); // Replace TOKEN with your actual token

        try {
            URL url = new URL(String.format(CANCEL_ORDER_URL, orderId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.connect();

            // Get the response code
            int responseCode = connection.getResponseCode();

            logger.info("Response Code: " + responseCode);
            // Optional: Handle the response code or response data
            if (responseCode != HttpURLConnection.HTTP_OK) {

                // Handle other response codes or errors
                throw new Exception("Request failed with HTTP code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
