package com.samruddhi.trading.equities.services;


import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;
import common.JsonParser;

import java.io.InputStream;
import java.net.URI;
        import java.net.http.HttpClient;
        import java.net.http.HttpRequest;
        import java.net.http.HttpResponse;
        import java.io.IOException;

public class StreamingOptionQuoteServiceImpl implements StreamingOptionQuoteService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public  OptionData getOptionQuote(String url, String ticker, String optionStrike) {
        OptionData optionData = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))

                .header("Accept", "application/vnd.tradestation.streams.v2+json")
                .build();

        // Use a BodyHandler to process the streaming response
        HttpResponse.BodyHandler<InputStream> responseBodyHandler = HttpResponse.BodyHandlers.ofInputStream();

        try {
            HttpResponse<InputStream> response = httpClient.send(request, responseBodyHandler);
            // Assuming the response body is an InputStream
            try (InputStream stream = response.body()) {
                // Process the stream continuously
                String optionResponsejson = buildResponseJson(stream);
                optionData = JsonParser.getOptionQuote(optionResponsejson);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return optionData;
    }

    private  String buildResponseJson(java.io.InputStream stream) throws IOException {
        // Implement the logic to read from the stream
        // For example, reading continuously until a certain condition is met
        byte[] buffer = new byte[1024];
        int read;
        StringBuilder responseJson = new StringBuilder();
        while ((read = stream.read(buffer)) != -1) {
            String chunk = new String(buffer, 0, read);
            responseJson.append(chunk);
        }
        return responseJson.toString();
    }

    public static void main(String[] args) {
        String url = "https://api.tradestation.com/v3/marketdata/stream/options/quotes?legs%5B0%5D.Symbol=";
        // Replace {underlying} with the actual underlying symbol you're interested in

        StreamingOptionQuoteServiceImpl streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        streamingOptionQuoteService.getOptionQuote(url, "MSFT", "500");
    }
}
