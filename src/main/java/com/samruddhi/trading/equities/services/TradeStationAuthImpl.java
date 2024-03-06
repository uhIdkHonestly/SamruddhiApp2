package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.services.base.Authenticator;
import common.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.util.EntityUtils;


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
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String TOKEN_ENDPOINT = "https://signin.tradestation.com/oauth/token";

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
       /* try {
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
        }*/

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder(TOKEN_ENDPOINT);
            uriBuilder.addParameter("grant_type", "authorization_code");

            URI uri = uriBuilder.build();

            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

            String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            postRequest.setHeader("Authorization", "Basic " + encodedCredentials);

            StringEntity entity = new StringEntity("client_id=" + CLIENT_ID
                    //+ "&grant_type=authorization_code"
                    + "&redirect_uri=httpd://localhost"
                    + "&grant_type=authorization_code"
                    + "&client_secret=" + CLIENT_SECRET, StandardCharsets.UTF_8);
            postRequest.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(postRequest);

            String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("responseString" + responseString);
            if (response.getStatusLine().getStatusCode() == 200) {

                accessTokenMayBe = JsonParser.getJsonTagValue(responseString, ACCESS_TOKEN);
                return accessTokenMayBe/* access token extracted from response */;
            } else {
                throw new IOException("Failed to obtain access token: " + responseString);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Getting access token from TradeStation failed" + e.getMessage());
        }
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

    public static void main(String[] args) {
        TradeStationAuthImpl authImpl = new TradeStationAuthImpl();
        authImpl.getAccessToken();
    }
}
