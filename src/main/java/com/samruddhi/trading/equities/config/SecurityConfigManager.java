package com.samruddhi.trading.equities.config;

import com.samruddhi.trading.equities.encryption.Encrypter;
import com.samruddhi.trading.equities.services.TradeStationAuthImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SecurityConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigManager.class);
    private static SecurityConfigManager instance;
    private final Properties properties;

    private final SecretKey secretKey;

    private final Encrypter encrypter = new Encrypter();

    private SecurityConfigManager() {

        secretKey = encrypter.loadSecretKey(new File(this.getClass().getResource("/secret.key").getPath()));

        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hashed.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("application.properties file not found in classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading config.properties", e);
        }
    }

    public static synchronized SecurityConfigManager getInstance() {
        if (instance == null) {
            instance = new SecurityConfigManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        try {
            return encrypter.decrypt(properties.getProperty(key), secretKey);
        } catch(Exception e) {
            logger.error("Error retrieving requested security key:" + key);
        }
        return null;

    }

    public static void main(String[] args) {
        String clientId = SecurityConfigManager.getInstance().getProperty("CLIENT_ID");
        String secret = SecurityConfigManager.getInstance().getProperty("SECRET");
        System.out.println("ClientId:" + clientId);
        System.out.println("secret:" + secret);
    }
}
