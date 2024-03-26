package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class TradeWorkerPriceHelper {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public static boolean hasDroppedByGivenPercentage(OrderFillStatus buyFillStatus, Bar currentOptionPriceBar, double threshHoldPercentage) {

        BigDecimal buyPrice = new BigDecimal(buyFillStatus.getFillPrice());
        BigDecimal currentPrice = new BigDecimal(currentOptionPriceBar.getClose());

        // Subtract stock prices

        int compared = currentPrice.compareTo(buyPrice);
        if (compared > 0)
            return false;
        else {
            // How much % price drop is acceptable
            double acceptableLowerPrice = (buyFillStatus.getPriceOfUnderlying() * (1 - threshHoldPercentage));
            logger.info("acceptableLowerPrice {} :", acceptableLowerPrice);
            if (currentOptionPriceBar.getClose() < acceptableLowerPrice) {
                logger.info("For ticker {} the current price {} lower than buy price {} * threshhold % {}. Hence we must sell the stock or close option instrument", buyFillStatus.getTicker(), currentPrice, buyPrice, threshHoldPercentage);
                return true;
            }
        }
        return false;
    }

    public static boolean hasIncreasedByGivenPercentage(OrderFillStatus buyFillStatus, Bar currentOptionPriceBar, double threshHoldPercentage) {

        BigDecimal buyPrice = new BigDecimal(buyFillStatus.getFillPrice());
        BigDecimal currentPrice = new BigDecimal(currentOptionPriceBar.getClose());

        // Subtract stock prices

        int compared = currentPrice.compareTo(buyPrice);
        if (compared < 0)
            return false;
        else {
            // How much % price increase is acceptable
            double acceptableHigherPrice = (buyFillStatus.getPriceOfUnderlying() * (1 + threshHoldPercentage));
            logger.info("acceptableHigherPrice {} :", acceptableHigherPrice);
            if (currentOptionPriceBar.getClose() > acceptableHigherPrice) {
                logger.info("For ticker {} the current price {} higher than buy price {} * threshhold % {}. Hence we must sell the stock or close option instrument", buyFillStatus.getTicker(), currentPrice, buyPrice, threshHoldPercentage);
                return true;
            }
        }
        return false;
    }
}
