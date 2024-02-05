package com.samruddhi.trading.equities.core;

import com.samruddhi.trading.equities.core.base.HistoricalDataService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HistoricalDataServiceImpl implements HistoricalDataService {

    private final String API_URL = "";

    /**
     * Historical stock data over multiple days as Json string
     */
    public String getHistoricalData(String accessToken) throws IOException {
        // Replace with the actual symbol, interval, and date range
        String symbol = "AAPL";
        String interval = "1min";
        String startDate = "20230101";
        String endDate = "20230102";

        URL url = new URL(API_URL + "data/historical/" + symbol + "/" + interval + "?StartDate=" + startDate + "&EndDate=" + endDate);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
