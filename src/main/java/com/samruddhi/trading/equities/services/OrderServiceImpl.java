package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.OptionTradeAction;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.logic.NumberFormatHelper;
import com.samruddhi.trading.equities.services.base.OrderService;
import com.samruddhi.trading.equities.tradingmode.TradingMode;
import common.JsonParser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private String PLACE_ORDER_URL = TradingMode.placeOrderUrl();
    private String CANCEL_ORDER_URL = TradingMode.cancelOrderUrl();

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
    private final String PAYLOAD_PUT_STR = "{\n" +
            "\"Quantity\": \"%s\",\n" +
            "\"LimitPrice\": \"%s\"\n" +
            "}";

    String PAYLOAD_POST_STR = "{"
            + "\"AccountID\": \"%s\","
            + "\"LimitPrice\": \"%s\","
            + "\"Legs\":  %s,"
            + "\"Symbol\": \"%s\","
            + "\"Quantity\": \"%s\","
            + "\"OrderType\": \"%s\","
            + "\"TradeAction\": \"%s\","
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
            logger.info("Response Body: " + responseBody);
            if (response.code() != HttpURLConnection.HTTP_OK) {
                // Handle other response codes or errors
                throw new Exception("Buy Request failed with HTTP code: " + response.code() + responseBody);
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
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    String responseBody = response.body().string();
                    logger.info(responseBody);
                    throw new Exception("Request failed with HTTP code: " + response.code() + responseBody);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public UpdateOrderResponse updateOrder(String orderId, double limitPrice) throws Exception {

        OkHttpClient client = new OkHttpClient();

        String token = TradeStationAuthImpl.getInstance().getAccessToken().get();
        // Define MediaType for the request body
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Create request body
        String jsonBody = String.format(PAYLOAD_PUT_STR, orderId, limitPrice);
        RequestBody body = RequestBody.create(jsonBody, JSON);

        // Build PUT request
        Request request = new Request.Builder()
                .url(PLACE_ORDER_URL) // Replace with your URL
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {

            if (response.code() != HttpURLConnection.HTTP_OK) {
                String responsBody = response.body().string();
                // Handle other response codes or errors
                logger.info(responsBody);
                throw new Exception("Update Request failed with HTTP code: " + response.code() + responsBody);
            }
            // Handle the response as needed
            return JsonParser.getUpdateOrderResponse(response.toString());
        }
    }

    private String formatPayload(PlaceOrderPayload payload) {

        String formattedMessage = String.format(PAYLOAD_POST_STR, ConfigManager.getInstance().getProperty("account.id"),
                NumberFormatHelper.formatDecimals(payload.getLimitPrice(), 1), // TO DO fix decimals based on price
                payload.getLegs(),
                payload.getUnderlyingTicker(),
                payload.getQuantity(),
                payload.getOrderType(),
                OptionTradeAction.getOptionTradeAction(payload.getTradeAction()));
        logger.info("Payload: {}", formattedMessage);
        return formattedMessage;
    }
}
