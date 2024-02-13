package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.services.base.Authenticator;
import common.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * curl --request POST \
 * --url 'https://signin.tradestation.com/oauth/token' \
 * --header 'content-type: application/x-www-form-urlencoded' \
 * --data 'grant_type=authorization_code' \
 * --data 'client_id=YOUR_CLIENT_ID' \
 * --data 'client_secret=YOUR_CLIENT_SECRET' \
 * --data 'code=YOUR_AUTHORIZATION_CODE' \
 * --data 'redirect_uri=https://exampleclientapp/callback'
 * <p>
 * {
 * "access_token": "eGlhc2xv...MHJMaA",
 * "refresh_token": "eGlhc2xv...wGVFPQ",
 * "id_token": "vozT2Ix...wGVFPQ",
 * "token_type": "Bearer",
 * "scope": "openid profile MarketData ReadAccount Trade Crypto offline_access",
 * "expires_in": 1200
 * }
 */
public class TradeStationAuthImpl implements Authenticator {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String CLIENT_ID = "your_client_id";
    private static final String CLIENT_SECRET = "your_client_secret";
    private static final String TOKEN_ENDPOINT = "https://api.radestation.com/oauth/token";

    private static Authenticator instance;

    private TradeStationAuthImpl() {
    }

    public static synchronized Authenticator getInstance() {
        if (instance == null) {
            instance = new TradeStationAuthImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> getAccessToken() {
        Optional<String> accessTokenMayBe = Optional.empty();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TOKEN_ENDPOINT))
                    .header("Authorization", basicAuthHeader())
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .timeout(Duration.ofSeconds(10)) // Set a timeout for the request
                    .build();


            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Read and output the response
            String jsonResponse = response.body();
            System.out.println("Response: " + jsonResponse);

            // TODO: Extract the access token from the response and use it to make authenticated requests to the API
            accessTokenMayBe = JsonParser.getJsonTagValue(jsonResponse, ACCESS_TOKEN);

        } catch (IOException  | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Getting access token from TradeStation failed" + e.getMessage());
        }
        return accessTokenMayBe;
    }

    private static String basicAuthHeader() {
        String encodedCredentials = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        return "Basic " + encodedCredentials;
    }

    @Override
    /** return the API key we get from TradeStation
     *
     */
    public String getApiKey() {
        return "acklsdkadkkadadladda";
    }
}
