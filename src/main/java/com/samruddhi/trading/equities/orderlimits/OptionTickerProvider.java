package com.samruddhi.trading.equities.orderlimits;


import static com.samruddhi.trading.equities.orderlimits.NearestOptionStrikePrice.getNearestOptionCallStrikePrice;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.logic.OptionExpiryPeriod;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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

    public static NextStrikePrice getNextOptionTicker(String ticker, double price, char callOrPut) {
        String nextExpiryDate = OptionExpiryPeriod.hasDailyOptions(ticker) ? getToday() : getNextFriday();
        double strikePrice = nextStrikePrice(ticker, price);

        // "Symbol": "MSFT 211217P332.5",
        StringBuilder fulltickerSb = new StringBuilder(ticker);
        fulltickerSb.append(" ");
        fulltickerSb.append(nextExpiryDate);
        fulltickerSb.append(callOrPut);
        fulltickerSb.append(strikePrice);
        // We may not need this piece
        StringBuilder tickerWithDateSb = new StringBuilder(strikePrice + "");
        tickerWithDateSb.append(nextExpiryDate);
        tickerWithDateSb.append(callOrPut);

        return new NextStrikePrice(fulltickerSb.toString(), tickerWithDateSb.toString(), ticker);
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
     * @return
     */
    private static double nextStrikePrice(String ticker, double price) {
        double closestStrikePrice = 0;

        // ETFs are rounded down to the nearest number, other logic based on Pricing needs fixing
        if (OptionExpiryPeriod.hasDailyOptions(ticker)) {
            closestStrikePrice = getNearestOptionCallStrikePrice(price, 1);
        } else {
            int priceInt = (int) Math.floor(price);
            if (priceInt < 30) {
                closestStrikePrice = getNearestOptionCallStrikePrice(price, 0.5);
            } else if (priceInt < 120) {
                closestStrikePrice = getNearestOptionCallStrikePrice(price, 1);
            } else if (priceInt < 200) {
                closestStrikePrice = getNearestOptionCallStrikePrice(price, 2.5);
            } else if (priceInt < 500) {
                closestStrikePrice = getNearestOptionCallStrikePrice(price, 5);
            } else {
                closestStrikePrice = getNearestOptionCallStrikePrice(price, 10);
            }
        }
        return closestStrikePrice;
    }

}
