package com.samruddhi.trading.equities.studies;

import com.samruddhi.trading.equities.domain.Bar;

import java.util.List;

public class RSICalculator {

    public static double calculateRSI(List<Bar> bars, int period) {
        if (bars.size() < period) {
            throw new IllegalArgumentException("Not enough bars to calculate RSI.");
        }

        double gain = 0, loss = 0;
        for (int i = 1; i < bars.size(); i++) {
            double change = bars.get(i).getClose() - bars.get(i - 1).getClose();
            if (change > 0) {
                gain += change;
            } else {
                loss -= change;
            }
        }

        double avgGain = gain / period;
        double avgLoss = loss / period;

        // Avoid division by zero; if avgLoss is 0, RSI is considered to be 100 as per convention.
        if (avgLoss == 0) {
            return 100;
        }

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public static void main(String[] args) {
        // Example usage with dummy data
        List<Bar> bars = List.of(
                new Bar(100),
                new Bar(105),
                new Bar(103)
                // Add more bars as per your data
        );

        try {
            double rsi = calculateRSI(bars, 14);
            System.out.println("RSI: " + rsi);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
