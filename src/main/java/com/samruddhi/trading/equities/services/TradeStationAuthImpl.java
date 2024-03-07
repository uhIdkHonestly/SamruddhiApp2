package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.config.SecurityConfigManager;
import com.samruddhi.trading.equities.services.base.Authenticator;
import common.JsonParser;
import okhttp3.*;
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
import java.util.*;

import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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
    private static final String CLIENT_ID_FROM_STEP1 = "FakGIXa4s9Rr89aC";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private static final String TOKEN_ENDPOINT = "https://signin.tradestation.com/oauth/token";

    private static final String AUTHORIZATION_CODE_ENDPOINT = "https://signin.tradestation.com/authorize";


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

    /**
     * This does not work, needs a browesr
     */
    public String authorize() throws Exception {

        //The below works Use in Browser, needs a working localhost:80 http server that recieves redirect
        // https://signin.tradestation.com/authorize?response_type=code&client_id=COKKzfMyHCbSncPo5LOXtPKEzo2z7VtC&redirect_uri=http://localhost:80&audience=https://api.tradestation.com&state=STATE&scope=openid%20offline_access%20profile%20MarketData%20ReadAccount%20Trade%20Matrix%20OptionSpreads

       /* // Programatic accss to get CODE will not work as TS resttrcts it and needs a browser
        // Initialize OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();

        // Build the authorization request URL
        String authorizationUrl = "https://signin.tradestation.com/authorize"
                //+ "?client_id=" + CLIENT_ID_API_KEY
                + "&redirect_uri=" + "http://localhost"
                + "&response_type=code"
                + "&audience=https://api.tradestation.com"
                + "&scope=" + "openid profile offline_access MarketData ReadAccount Trade OptionSpreads";

         //The below works Use in Browser, needs a working localhost:80 http server that recieves redirect
        // https://signin.tradestation.com/authorize?response_type=code&client_id=COKKzfMyHCbSncPo5LOXtPKEzo2z7VtC&redirect_uri=http://localhost:80&audience=https://api.tradestation.com&state=STATE&scope=openid%20offline_access%20profile%20MarketData%20ReadAccount%20Trade%20Matrix%20OptionSpreads

        // Create a request object
        Request request = new Request.Builder().url(authorizationUrl).build();
        String responseString = "";
        // Send the authorization request and handle the response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Handle unsuccessful response (e.g., log error)
                throw new Exception();
            }
            ResponseBody responseBody = response.body();
            responseString = responseBody.string();
            logger.info(responseString);
            System.out.println(responseString);
        } catch (IOException e) {
            // Handle potential IO exceptions (e.g., log error)
            throw e;
        }
        return responseString;*/

        return null;
    }

    @Override
    public Optional<String> getAccessToken() {
        // TO DO Enable me... after tests
        //if (cachedAccessToken.containsKey(ACCESS_TOKEN))
        //    return Optional.of(cachedAccessToken.get(ACCESS_TOKEN));

        if (ConfigManager.getInstance().getProperty("ACCESS_TOKEN").length() > 0)
            return Optional.of(ConfigManager.getInstance().getProperty("ACCESS_TOKEN"));

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

                accessTokenMayBe = JsonParser.getJsonTagValue(responseString, ACCESS_TOKEN);
                Optional<String> refreshTokenMayBe = JsonParser.getJsonTagValue(responseString, REFRESH_TOKEN);
                logger.info("Access Token: " + accessTokenMayBe);
                cachedAccessToken.put(ACCESS_TOKEN, accessTokenMayBe.get());
                cachedAccessToken.put(REFRESH_TOKEN, refreshTokenMayBe.get());
                return accessTokenMayBe/* access token extracted from response */;
            } else {
                throw new IOException("Failed to obtain access token: " + responseString);
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Getting access token from TradeStation failed" + e.getMessage());
        }
    }

    public String getRefreshToken() {
        return cachedAccessToken.get(REFRESH_TOKEN);

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
