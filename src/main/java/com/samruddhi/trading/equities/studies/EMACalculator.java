package com.samruddhi.trading.equities.studies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.services.TradeStationAuthImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class EMACalculator {

    private static final Logger logger = LoggerFactory.getLogger(EMACalculator.class);

    /** The bars will have upto 50 days, use parameter duration to pick 5, 9, 50 days
     *
     * @param bars
     * @param duration
     * @return
     */

    public static double calculateEMAs(List<Bar> bars, int duration) {
        // Implement EMA calculation logic for 5, 13, and 50 periods using the historical data
        int emaPeriod = duration;
        double multiplier = 2.0 / (emaPeriod + 1);
        double ema = 0.0;

        int start = bars.size() - duration;
        if(start < 0)
            throw new IllegalArgumentException("start index must be positive");

        for (int i = start; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            double closePrice = Double.valueOf(bar.getClose());
            if (i == 0) {
                ema = closePrice;
            } else {
                ema = (closePrice - ema) * multiplier + ema;
            }
            i++;
            // Display or use ema5 as needed
            logger.info(String.format("EMA(%s) for date %s is %s", duration, bar.getTimeStamp(), ema));
        }

        return ema;
    }
}
