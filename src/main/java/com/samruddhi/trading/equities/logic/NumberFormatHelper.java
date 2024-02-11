package com.samruddhi.trading.equities.logic;

public class NumberFormatHelper {
    /** formats given number to 5 integral and 3 decimal format as needed by TICKER
     *
     * @param number
     * @return
     */
    public static String formatOptionStrike(double number) {
        int integerPart = (int) number; // Get the integer part of the number
        int fractionalPart = (int) Math.round((number - integerPart) * 1000); // Get the fractional part, rounded and scaled

        // Format the integer part to be exactly 5 digits, padding with zeros if necessary
        String formattedIntegerPart = String.format("%05d", integerPart);

        // Format the fractional part to be exactly 3 digits
        // Note: The fractional part is already scaled to 3 digits, so just convert to string
        String formattedFractionalPart = String.format("%03d", fractionalPart);

        // Concatenate the formatted parts to get the final 8-digit string
        return formattedIntegerPart + formattedFractionalPart;
    }

    public static void main(String[] args) {
        double number = 442.13;
        String formattedNumber = formatOptionStrike(number);
        System.out.println("formattedNumber" + formattedNumber); // Output will be 00442
    }
}
