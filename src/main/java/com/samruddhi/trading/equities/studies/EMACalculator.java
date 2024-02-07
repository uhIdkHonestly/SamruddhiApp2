package com.samruddhi.trading.equities.studies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samruddhi.trading.equities.domain.Bar;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class EMACalculator {


    /**
    public void calculateEMAs() {
        // Replace with your actual API token
        String token = "YOUR_API_TOKEN";

        // API endpoint URL for historical data (adjust instrument and date range as needed)
        String apiUrl = "https://api.tradestation.com/v3/marketdata/barcharts/MSFT?startDateTime=2022-01-01T00%3A00%3A00.000Z&endDateTime=2022-12-31T23%3A59%3A59.999Z";

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

                ObjectMapper objectMapper = new ObjectMapper();

                // Read the JSON array and map it to a List of Person objects
                List<Bar> bars = objectMapper.readValue(responseBody, new TypeReference<List<Bar>>() {
                });


                // Calculate EMAs for 5, 13, and 50 periods (you can implement this part)
                calculateEMAs(bars);
            } else {
                System.err.println("Error: HTTP status code " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */

    /** The bars will have upto 50 days, use parameter duration to pick 5, 9, 50 days
     *
     * @param bars
     * @param duration
     * @return
     */

    public static double calculateEMAs(List<Bar> bars, int duration) {
        // Implement EMA calculation logic for 5, 13, and 50 periods using the historical data
        // You can use a library like Apache Commons Math or implement it manually
        // Here's a simplified example of calculating a 5-period EMA:
        int emaPeriod5 = 5;
        double multiplier5 = 2.0 / (emaPeriod5 + 1);
        double ema5 = 0.0;

        int start = bars.size() - duration;
        if(start < 0)
            throw new IllegalArgumentException("start index must be positive");

        for (int i = start; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            double closePrice = Double.valueOf(bar.getClose());
            if (i == 0) {
                ema5 = closePrice;
            } else {
                ema5 = (closePrice - ema5) * multiplier5 + ema5;
            }
            i++;
            // Display or use ema5 as needed
            System.out.println(String.format("EMA(%s) for date %s is %s", duration, bar.getTimeStamp(), ema5));
        }
        // Implement genrically similar calculations for 13 and 50 EMAs

        return ema5;
    }
}
