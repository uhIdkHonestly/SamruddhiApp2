package com.samruddhi.trading.equities.services;


import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.logic.FileWriter;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;
import common.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class StreamingOptionQuoteServiceImpl implements StreamingOptionQuoteService {

    private static final Logger logger = LoggerFactory.getLogger(StreamingOptionQuoteServiceImpl.class);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String OPTION_QUOTES_URL = "https://api.tradestation.com/v3/marketdata/stream/options/quotes";

    private final String token;

    public StreamingOptionQuoteServiceImpl() {

        token = TradeStationAuthImpl.getInstance().getAccessToken().get();
    }

    @Override
    public OptionData getOptionQuote(String optionTicker) throws Exception {
        OkHttpClient client = new OkHttpClient();
        OptionData optionData = null;
        try {
            HttpUrl url = HttpUrl.parse(OPTION_QUOTES_URL).newBuilder()
                    .addQueryParameter("legs[0].Symbol", optionTicker)
                    .build();

            // Constructing the request
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json") // Example of a common header
                    .header("Authorization", "Bearer " + token) // Replace 'your_token_here' with your actual token
                    .build();

            try (Response response = client.newCall(request).execute()) {
                // Output the response body
                String optionResponsejson =  buildResponseJson(response.body().byteStream());
                FileWriter.writeToFile(optionResponsejson);
                optionData = JsonParser.getOptionQuote(optionResponsejson);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        } catch (Exception  e1) {
            e1.printStackTrace();
            throw e1;
        }

        return optionData;
    }

    private String buildResponseJson(java.io.InputStream stream) throws IOException {
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

    public static void main(String[] args) throws Exception {
        // Replace {underlying} with the actual underlying symbol you're interested in

        StreamingOptionQuoteServiceImpl streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        OptionData optionData = streamingOptionQuoteService.getOptionQuote("AAPL 240308C167.5" );
        logger.info(optionData.toString());
    }
}
