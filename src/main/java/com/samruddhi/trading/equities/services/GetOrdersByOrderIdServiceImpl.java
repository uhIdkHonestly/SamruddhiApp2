package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.exceptions.GetOrdersException;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;
import com.samruddhi.trading.equities.tradingmode.TradingMode;
import common.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class GetOrdersByOrderIdServiceImpl implements GetOrdersByOrderIdService {

    private static final Logger logger = LoggerFactory.getLogger(GetOrdersByOrderIdService.class);

    private static final String GET_ORDERS_URL = TradingMode.getOrdersUrl();

    @Override
    public GetOrdersByOrderIdResponse getOrderFillStatus(String orderId) throws GetOrdersException {
        // Replace with your actual API token
        String token = TradeStationAuthImpl.getInstance().getAccessToken().get();
        String apiUrl = String.format(GET_ORDERS_URL, ConfigManager.getInstance().getProperty("account.id"), orderId);

        try {
            HttpResponse<String> response = createHttpRequest(apiUrl, token);
            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                // Parse the JSON response
                String responseBody = response.body();
                logger.info("Response JSON:");
                logger.info(responseBody);
                return JsonParser.getOrdersByIdResponse(responseBody);
            } else {
                System.err.println("Error: HTTP status code " + response.statusCode());
                throw new GetOrdersException("Http error non 200 while getting orders by Id" + response.statusCode()) ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new GetOrdersException(e.getMessage());
        }
    }

    private HttpResponse<String> createHttpRequest(String apiUrl, String token) throws Exception {

        // Create an HttpClient with a custom header for Authorization
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(30, ChronoUnit.MINUTES))
                .build();

        // Create the HTTP request with the Authorization header
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
