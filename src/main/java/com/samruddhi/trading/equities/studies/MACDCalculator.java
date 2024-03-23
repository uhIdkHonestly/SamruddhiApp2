package com.samruddhi.trading.equities.studies;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MACDCalculator {

    private static final Logger logger = LoggerFactory.getLogger(MACDCalculator.class);

    public static double computeEMA(List<Bar> bars, int period) {
        double ema = bars.get(0).getClose();
        double multiplier = 2.0 / (period + 1);
        for (int i = 1; i < bars.size(); i++) {
            ema = ((bars.get(i).getClose() - ema) * multiplier) + ema;
        }
        return ema;
    }

    public static double[] computeMACD(List<Bar> bars, int shortPeriod, int longPeriod, int signalPeriod) {
        double shortEMA = computeEMA(bars.subList(Math.max(bars.size() - shortPeriod, 0), bars.size()), shortPeriod);
        double longEMA = computeEMA(bars.subList(Math.max(bars.size() - longPeriod, 0), bars.size()), longPeriod);
        double macd = shortEMA - longEMA;
        double signalLine = computeEMA(bars.subList(Math.max(bars.size() - signalPeriod, 0), bars.size()), signalPeriod);

        return new double[]{macd, signalLine};
    }

    public static boolean isMACDTrendBullish(double macd, double signalLine) {
        String bullish =  macd > signalLine ? "Bullish" : "Bearish";
        logger.info(bullish);
        return macd > signalLine;
    }

    public static void main(String[] args) {
        // Example usage with dummy data
        List<Bar> bars = List.of(
                new Bar(100),
                new Bar(101),
                new Bar(102)
                // Add more bars as per your data
        );

        double[] macdValues = computeMACD(bars, 12, 26, 9);
        double macd = macdValues[0];
        double signalLine = macdValues[1];

        System.out.println("MACD: " + macd + ", Signal Line: " + signalLine);
        System.out.println("Trend: " + isMACDTrendBullish(macd, signalLine));
    }
}
