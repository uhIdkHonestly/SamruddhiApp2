package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.services.base.OrderService;
import common.JsonParser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private String PLACE_ORDER_URL = "https://api.tradestation.com/v3/orderexecution/orders";
    private String CANCEL_ORDER_URL = "https://api.tradestation.com/v3/orderexecution/orders/%s";

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
    private final String PAYLOAD_STR = "{"
            + "\"AccountID\": \"%S\","
            + "\"Symbol\": \"%S\","
            + "\"Quantity\": \"%s\","
            + "\"OrderType\": \"%S\","
            + "\"TradeAction\": \"%S\","
            + "\"TimeInForce\": {\"Duration\": \"DAY\"},"
            + "\"Route\": \"Intelligent\""
            + "}";

    /**
     * Send an Option buy order to the broker
     */
    public PlaceOrderResponse placeOrder(PlaceOrderPayload orderPayload) throws Exception {

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Create request body with specified media type and JSON payload
        RequestBody body = RequestBody.create(formatPayload(orderPayload), JSON);

        String token = TradeStationAuthImpl.getInstance().getAccessToken().get();

        Request request = new Request.Builder()
                .url(PLACE_ORDER_URL)
                .post(body) // Set the request method to POST and provide the request body
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("Response Body: " + response.body().string());
            if (response.code() != HttpURLConnection.HTTP_OK) {
                // Handle other response codes or errors
                logger.info(response.body().string());
                throw new Exception("Buy Request failed with HTTP code: " + response.code() + response.body().string());
            }
            return JsonParser.getPlaceOrderResponse(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Cancel an existing Option buy order
     */
    @Override
    public void cancelOrder(String orderId) throws Exception {
        String token = TradeStationAuthImpl.getInstance().getAccessToken().get();

        try {
            OkHttpClient client = new OkHttpClient();
            URL url = new URL(String.format(CANCEL_ORDER_URL, orderId));
            Request request = new Request.Builder()
                    .url(url)
                    .header("Content-Type", "application/json") // Add Content-Type header
                    .header("Authorization", "Bearer " + token)// Add Authorization header
                    .delete() // This is where we specify the HTTP method DELETE
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    // Handle other response codes or errors
                    logger.info(response.body().string());
                    throw new Exception("Request failed with HTTP code: " + response.code() + response.body().string());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String formatPayload(PlaceOrderPayload payload) {
        String payloadStr = "{"
                + "\"AccountID\": \"%s\","
                + "\"Symbol\": \"%s\","
                + "\"Quantity\": \"%s\","
                + "\"OrderType\": \"%s\","
                + "\"TradeAction\": \"%s\","
                + "\"TimeInForce\": {\"Duration\": \"DAY\"},"
                + "\"Route\": \"Intelligent\""
                + "}";
        String formattedMessage = String.format(PAYLOAD_STR, ConfigManager.getInstance().getProperty("account.id"),
                payload.getSymbol(),
                payload.getQuantity(),
                payload.getOrderType(),
                payload.getTradeAction());
        logger.info("Payload: {}", payload);
        return formattedMessage;
    }
}
