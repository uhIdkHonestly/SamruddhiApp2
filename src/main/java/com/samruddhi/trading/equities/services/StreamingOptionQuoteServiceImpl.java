package com.samruddhi.trading.equities.services;


import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.logic.FileDataWriter;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;
import com.samruddhi.trading.equities.tradingmode.TradingMode;
import common.JsonParser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StreamingOptionQuoteServiceImpl implements StreamingOptionQuoteService {

    private static final Logger logger = LoggerFactory.getLogger(StreamingOptionQuoteServiceImpl.class);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String OPTION_QUOTES_URL = TradingMode.optionsQuoteUrl();

    private final String token;

    public StreamingOptionQuoteServiceImpl() {
        token = TradeStationAuthImpl.getInstance().getAccessToken().get();
    }

    @Override
    public OptionData getOptionQuote(String optionTicker) throws Exception {

        OkHttpClient client =  new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
                .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
                .build();
        OptionData optionData = null;
        try {
            HttpUrl url = HttpUrl.parse(OPTION_QUOTES_URL).newBuilder()
                    .addQueryParameter("legs[0].Symbol", optionTicker)
                    .build();

            // Constructing the request
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                try (ResponseBody responseBody = response.body();
                     Reader reader = responseBody.charStream();
                     BufferedReader bufferedReader = new BufferedReader(reader)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // Why is this
                        if(line.contains("Heartbeat"))
                            break;
                        stringBuilder.append(line);
                    }
                    String optionResponseJson = stringBuilder.toString();
                    FileDataWriter.writeToFile(optionResponseJson);
                    optionData = JsonParser.getOptionQuote(optionResponseJson);
                }
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
            logger.info("read " + read + "");
            String chunk = new String(buffer, 0, read);
            responseJson.append(chunk);
        }
        return responseJson.toString();
    }

    public static void main(String[] args) throws Exception {
        // Replace {underlying} with the actual underlying symbol you're interested in
        StreamingOptionQuoteServiceImpl streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        // OptionData optionData = streamingOptionQuoteService.getOptionQuote("AAPL 240308C167.5" );

        OptionData optionData = streamingOptionQuoteService.getOptionQuote("NVDA 240308C885" );

        logger.info(optionData.toString());
    }
}
