package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.config.SecurityConfigManager;
import com.samruddhi.trading.equities.services.base.Authenticator;
import com.sun.net.httpserver.HttpServer;
import common.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * ==  Request ===
 * curl --request POST \
 * --url 'https://signin.tradestation.com/oauth/token' \
 * --header 'content-type: application/x-www-form-urlencoded' \
 * --data 'grant_type=authorization_code' \
 * --data 'client_id=YOUR_CLIENT_ID' \
 * --data 'client_secret=YOUR_CLIENT_SECRET' \
 * --data 'code=YOUR_AUTHORIZATION_CODE' \
 * --data 'redirect_uri=http://localhost:80'
 * <p>
 * ==  Response ===
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
    private static final Logger logger = LoggerFactory.getLogger(TradeStationAuthImpl.class);
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private static final String  TOKEN_ENDPOINT = "https://signin.tradestation.com/oauth/token";

    private static final HashMap<String, String> cachedAccessToken = new HashMap<>();
    private static Authenticator instance;

    private TradeStationAuthImpl() {
        CLIENT_ID = SecurityConfigManager.getInstance().getProperty("CLIENT_ID");
        CLIENT_SECRET = SecurityConfigManager.getInstance().getProperty("SECRET");
    }

    public static synchronized Authenticator getInstance() {
        if (instance == null) {
            instance = new TradeStationAuthImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> getAccessToken() {
        // TO DO Enable me... after tests
        if (cachedAccessToken.containsKey(ACCESS_TOKEN_KEY))
            return Optional.of(cachedAccessToken.get(ACCESS_TOKEN_KEY));
        else if (ConfigManager.getInstance().getProperty(ACCESS_TOKEN_KEY).length() > 0) // TO REMOVE ME - this is for  initial testing
            return Optional.of(ConfigManager.getInstance().getProperty(ACCESS_TOKEN_KEY));

        Optional<String> accessTokenMayBe = Optional.empty();

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
                    + "&redirect_uri=http://localhost:80"
                    + "&code=" + ConfigManager.getInstance().getProperty("code")
                    + "&grant_type=authorization_code"
                    + "&client_secret=" + CLIENT_SECRET, StandardCharsets.UTF_8);
            postRequest.setEntity(entity);

            CloseableHttpResponse response = httpClient.execute(postRequest);

            String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (response.getStatusLine().getStatusCode() == 200) {

                accessTokenMayBe = JsonParser.getJsonTagValue(responseString, ACCESS_TOKEN_KEY);
                Optional<String> refreshTokenMayBe = JsonParser.getJsonTagValue(responseString, REFRESH_TOKEN_KEY);
                logger.info("Access Token: " + accessTokenMayBe);
                logger.info("Refresh Token: " + refreshTokenMayBe);
                cachedAccessToken.put(ACCESS_TOKEN_KEY, accessTokenMayBe.get());
                cachedAccessToken.put(REFRESH_TOKEN_KEY, refreshTokenMayBe.get());

                return accessTokenMayBe;
            } else {
                throw new IOException("Failed to obtain access token: " + responseString);
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Getting access token from TradeStation failed" + e.getMessage());
        }
    }

    public String getRefreshToken() {

        String refreshToken = cachedAccessToken.get(REFRESH_TOKEN_KEY);
        logger.info("refreshToken: " + refreshToken);
        // TO DO REMOVE ME AFTER TEST
        /*if(refreshToken == null) {
            refreshToken = "TlIEjBTksx45kRkGz5Swb6_cpOpNIZAR0WSDyebdvH3d-";
        }*/
        // TO DO REMOVE END Block
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_ENDPOINT))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedCredentials)
                .POST(BodyPublishers.ofString("grant_type=refresh_token&refresh_token=" + refreshToken + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            logger.info("Refresh Token Response Status Code: " + response.statusCode());
            String body = "";
            if (response.statusCode() == 200) {
                body = response.body();
                String accessToken = JsonParser.getJsonTag(response.body(), ACCESS_TOKEN_KEY);
                logger.info("Refreshed Access Token: " + accessToken);
                cachedAccessToken.put(ACCESS_TOKEN_KEY, accessToken);
                return accessToken;
            } else {
                throw new IOException("Failed to obtain refresh token: " + body);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Getting refreshed access token from TradeStation failed" + e.getMessage());
        }
    }

    private String basicAuthHeader() {
        String encodedCredentials = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
        return "Basic " + encodedCredentials;
    }

    private static void startServer() throws Exception {

        int port = 80; // You can change this port if needed
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String response = "<h1>Hello, world!</h1>";
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });
        server.start();
        System.out.println("Server started at http://localhost:" + port);
    }

    public static void main(String[] args) throws Exception {
        startServer();
        //TradeStationAuthImpl authImpl = new TradeStationAuthImpl();
        //authImpl.getAccessToken();

        Optional<String> accessToken = TradeStationAuthImpl.getInstance().getAccessToken();
        System.out.println(accessToken);
        String refreshToken = TradeStationAuthImpl.getInstance().getRefreshToken();
        System.out.println(refreshToken);
    }
}
