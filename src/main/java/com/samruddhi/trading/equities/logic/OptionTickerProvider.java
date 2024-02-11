package com.samruddhi.trading.equities.logic;


import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Example Options Tickers
 * To help illustrate how to read an options ticker, take a look at the following examples.
 * <p>
 * A January 21, 2022 Call Option for Uber with a $50 Strike Price
 * <p>
 * UBER220121C00050000 = UBER + 220121 + C + 00050000
 * <p>
 * Underlying Stock - UBER
 * Expiration Date - January 21st, 2022 or ‘220121’ (YYMMDD)
 * Option Type - Call or ‘C’
 * Strike Price - 00050000 (50000/1000) or $50
 * <p>
 * A November 19, 2021 Put Option for Ford with a $14 Strike Price
 * <p>
 * F211119P00014000 = F + 211119 + P + 00014000
 * <p>
 * Underlying Stock - F (Ford)
 * Expiration Date - November 19th, 2021 or ‘211119’ (YYMMDD)
 * Option Type - Put or ‘P’
 * Strike Price - 00014000 (14000/1000) or $14
 * <p>
 * <p>
 * https://polygon.io/blog/how-to-read-a-stock-options-ticker
 */
public class OptionTickerProvider {

    private static final DateTimeFormatter dateformatddMMyyyy = DateTimeFormatter.ofPattern("yyMMdd");

    public static String getNextOptionTicker(String ticker, double price, char callOrPut) {
        String nextExpiryDate = OptionExpryPeriod.hasDailyOptions(ticker) ? getToday() : getNextFriday();
        String zeroPaddedPrice = NumberFormatHelper.formatOptionStrike(OptionTickerProvider.nextStrikePrice(ticker, price, 0));

        StringBuilder tickerSb = new StringBuilder(zeroPaddedPrice);
        tickerSb.append(nextExpiryDate);
        tickerSb.append(callOrPut);
        tickerSb.append(zeroPaddedPrice);

        return tickerSb.toString();
    }

    static String getToday() {
        LocalDate localDate = LocalDate.now();
        return dateformatddMMyyyy.format(localDate);
    }

    static String getNextFriday() {
        LocalDate today = LocalDate.now();
        LocalDate updatedDate = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        return dateformatddMMyyyy.format(updatedDate);
    }

    public static void main(String[] args) {
        OptionTickerProvider optionTickerProvider = new OptionTickerProvider();
        String adjustedDate = optionTickerProvider.getNextFriday();
        System.out.println("adjustedDate" + adjustedDate);
    }

    /**
     * Approximately calulate strike price to use using stock price range, need to validate this...
     * // https://www.optiontradingpedia.com/options_strike_price.htm#:~:text=There%20are%20four%20commonly%20used,expensive%20stocks%20generally%20above%20$200.
     *
     * @param ticker
     * @param price
     * @param offset
     * @return
     */
    public static double nextStrikePrice(String ticker, double price, int offset) {
        double floorPrice = 0;

        // ETFs are rounded down to the nearest number, other logic based on Pricing needs fixing
        if (OptionExpryPeriod.hasDailyOptions(ticker)) {
            floorPrice = (int) Math.floor(price);
        } else {
            int priceInt = (int) Math.floor(price);
            if (priceInt < 30) {
                floorPrice = priceInt - 0.50;
            } else if (priceInt < 120) {
                floorPrice = priceInt - 1;
            } else if (priceInt < 200) {
                floorPrice = priceInt - 2.5;
            } else if (priceInt < 500) {
                floorPrice = priceInt - 5;
            } else {
                floorPrice = priceInt - 10;
            }
            return floorPrice;
        }

        return floorPrice;
    }

}
