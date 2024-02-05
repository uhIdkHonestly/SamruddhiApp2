package com.samruddhi.trading.equities.services;

import common.JsonParser;
import com.samruddhi.trading.equities.services.base.Authenticator;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";
    private static final String AUTH_URL = "https://api.tradestation.com/v2/security/authorize";
    private static final String TOKEN_URL = "https://api.tradestation.com/v2/security/token";

    private static Authenticator instance;

    private TradeStationAuthImpl() {
    }

    public static synchronized Authenticator getInstance() {
        if (instance == null) {
            instance = new TradeStationAuthImpl();
        }
        return instance;
    }


    public Optional<String> getAccessToken() throws IOException {
        Optional<String> accessTokenMayBe = Optional.empty();
        try {
            HttpClient httpClient = HttpClients.createDefault();

            // Prepare the POST request for the access token
            HttpPost httpPost = new HttpPost(TOKEN_URL);

            // Add the required parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            params.add(new BasicNameValuePair("code", "AUTHORIZATION_CODE_RECEIVED_FROM_AUTH_URL"));
            params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
            params.add(new BasicNameValuePair("client_id", CLIENT_ID));
            params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // Execute the POST request
            HttpResponse response = httpClient.execute(httpPost);

            // Read and output the response
            String jsonResponse = EntityUtils.toString(response.getEntity());
            System.out.println("Response: " + jsonResponse);

            // TODO: Extract the access token from the response and use it to make authenticated requests to the API
            accessTokenMayBe = JsonParser.getJsonTagValue(jsonResponse, ACCESS_TOKEN);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessTokenMayBe;
    }
}
