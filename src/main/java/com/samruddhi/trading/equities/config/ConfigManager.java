package com.samruddhi.trading.equities.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150 = 0.1;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150 = 0.08;
    private final double ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50 = 0.12;
    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
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

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /** get acceptable price drop % */

    public double getAcceptablePriceDropPercent(double fillPrice, String ticker) {
        if(fillPrice < 50)
            return ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_50;
        else if( fillPrice < 150)
            return ACCEPTABLE_PRICE_DROP_PERCENT_UNDER_150;
        else
            return ACCEPTABLE_PRICE_DROP_PERCENT_ABOVE_150;
    }
}

