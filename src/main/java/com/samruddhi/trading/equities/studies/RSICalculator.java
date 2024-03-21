package com.samruddhi.trading.equities.studies;

import com.samruddhi.trading.equities.domain.Bar;

import java.util.List;

public class RSICalculator {

    public static double calculateRSI(List<Bar> bars, int period) {
        if (bars.size() < period) {
            throw new IllegalArgumentException("Not enough bars to calculate RSI.");
        }

        double averageGain = 0.0;
        double averageLoss = 0.0;

        for (int i = 1; i <= bars.size(); i++) {
            double change = bars.get(i).getClose() - bars.get(i - 1).getClose();
            if (change > 0) {
                averageGain += change;
            } else {
                averageLoss += Math.abs(change);
            }
        }

        averageGain /= period;
        averageLoss /= period;

        for (int i = period + 1; i < bars.size(); i++) {
            double change = bars.get(i).getClose() - bars.get(i - 1).getClose();
            if (change > 0) {
                averageGain = (averageGain * (period - 1) + change) / period;
                averageLoss = (averageLoss * (period - 1)) / period;
            } else {
                averageGain = (averageGain * (period - 1)) / period;
                averageLoss = (averageLoss * (period - 1) + Math.abs(change)) / period;
            }
        }

        double relativeStrength = averageGain / averageLoss;
        return 100.0 - (100.0 / (1.0 + relativeStrength));
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
