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
            double acceptableLowerPrice = buyFillStatus.getFillPrice() * threshHoldPercentage;

            if (acceptableLowerPrice < currentOptionPriceBar.getClose()) {
                logger.info("For ticker {} the current price {} lower than buy price {} * thresh hold % {}. Hence we must sell the contract", buyFillStatus.getTicker(), currentPrice, buyPrice, threshHoldPercentage);
                return true;
            }
        }
        return false;
    }
}
