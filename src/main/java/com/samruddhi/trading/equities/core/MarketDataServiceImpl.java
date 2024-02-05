package com.samruddhi.trading.equities.core;

import com.samruddhi.trading.equities.core.base.MarketDataService;
import com.samruddhi.trading.equities.domain.Bar;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MarketDataServiceImpl implements MarketDataService {

    private final String MARKET_DATA_URL = "https://api.tradestation.com/v3/marketdata/barcharts/%s?interval=%s&barsback=%s&sessiontemplate=Default";

    @Override
    public List<Bar> getStockDataBars(String ticker) throws Exception {
        // Replace with your actual API token
        String  token = TradeStationAuthImpl.getInstance().getAccessToken().get();

        // API endpoint URL
        String apiUrl = "";

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

        try {
            // Send the HTTP request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                // Parse the JSON response
                String responseBody = response.body();
                System.out.println("Response JSON:");
                System.out.println(responseBody);
            } else {
                System.err.println("Error: HTTP status code " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TO DO
        return null;
    }
}
