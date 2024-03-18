package com.samruddhi.trading.equities.orderlimits;

public class NearestOptionStrikePrice {

    public static double getNearestOptionCallStrikePrice(double stockValue, double strikeDifference) {
        // Calculate the number of times the strike difference fits into the stock value
        double strikeMultiple = stockValue / strikeDifference;

        // Round to the nearest whole number to find the closest strike multiple
        long nearestMultiple = Math.round(strikeMultiple);

        // Calculate the strike price using the nearest multiple
        double nearestStrikePrice = nearestMultiple * strikeDifference;

        return nearestStrikePrice;
    }

    public static void main(String[] args) {
        //double stockValue = 174.1;
        //double stockValue =167.81;
        //double strikeDifference = 2.5;

        double stockValue = 23.74;
        double strikeDifference  = 0.5;

        double nearestStrikePrice = getNearestOptionCallStrikePrice(stockValue, strikeDifference);
        System.out.println("Nearest Option Call Strike Price: $" + nearestStrikePrice);
    }

}

