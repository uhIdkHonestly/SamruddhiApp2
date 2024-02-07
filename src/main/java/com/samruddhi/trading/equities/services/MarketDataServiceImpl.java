package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import common.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class MarketDataServiceImpl implements MarketDataService {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataServiceImpl.class);

    public String TIME_UNIT_MINUTE = "Minute";
    public String TIME_UNIT_DAILY = "Daily";

    private final String MARKET_DATA_URL = "https://api.tradestation.com/v3/marketdata/barcharts/%s?interval=%s&unit=%s&barsback=%s&sessiontemplate=Default";

    /** For daily and minute chart please pass for  min -
     * 1, TIME_UNIT_MINUTE, 2
     * 1, TIME_UNIT_DAILY, 5, 13, 50
     *
     * @param ticker
     * @param durationType
     * @param duration
     * @param barsBack
     * @return
     * @throws Exception
     */
    @Override
    public List<Bar> getStockDataBars(String ticker, String durationType, int duration, int barsBack) throws Exception {
        // Replace with your actual API token
        String token = TradeStationAuthImpl.getInstance().getAccessToken().get();

        String apiUrl = String.format(MARKET_DATA_URL, ticker, 1, TIME_UNIT_DAILY, 2);

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
                logger.info("Response JSON:");
                logger.info(responseBody);
                return JsonParser.getListOfBars(responseBody, "bars");
            } else {
                System.err.println("Error: HTTP status code " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}