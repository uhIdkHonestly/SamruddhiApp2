package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.orderlimits.OptionTickerProvider;
import com.samruddhi.trading.equities.studies.EMACalculator;
import com.samruddhi.trading.equities.studies.MACDCalculator;
import com.samruddhi.trading.equities.studies.RSICalculator;

import java.util.List;

import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;

public class CallSellPointHelper {

    private final String ticker;

    CallSellPointHelper(String ticker) {
        this.ticker = ticker;
    }

    public boolean determineIfCallSellCriteriaMet(OrderFillStatus recentBuyFillStatus, List<Bar> minuteBars, List<Bar> dailyBars) throws Exception {
        double ema5 = EMACalculator.calculateEMAs(dailyBars, 5);
        double ema13 = EMACalculator.calculateEMAs(dailyBars, 13);
        double ema50 = EMACalculator.calculateEMAs(dailyBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = dailyBars.subList(24, dailyBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP in the future,
        double rsi = RSICalculator.calculateRSI(dailyBars.subList(36, dailyBars.size()), 14);

        if ((ema5 < ema50 || ema5 < ema13 || !isMacdBullish) ||
                (TradeWorkerPriceHelper.hasDroppedByGivenPercentage(recentBuyFillStatus, minuteBars.get(minuteBars.size() - 1), ConfigManager.getInstance().getAcceptablePriceDropPercent(recentBuyFillStatus.getFillPrice(), recentBuyFillStatus.getTicker())))) {
            return true;
        }
        return false;
    }

    public boolean determineIfPutSellCriteriaMet(OrderFillStatus recentBuyFillStatus, List<Bar> minuteBars, List<Bar> dailyBars) throws Exception {
        double ema5 = EMACalculator.calculateEMAs(dailyBars, 5);
        double ema13 = EMACalculator.calculateEMAs(dailyBars, 13);
        double ema50 = EMACalculator.calculateEMAs(dailyBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = dailyBars.subList(24, dailyBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP,
        double rsi = RSICalculator.calculateRSI(dailyBars.subList(36, dailyBars.size()), 14);
        boolean isRsiBullish = rsi > 40; // Fix me

        if ((ema5 > ema50 || ema5 > ema13 || isMacdBullish) ||
                (TradeWorkerPriceHelper.hasDroppedByGivenPercentage(recentBuyFillStatus, minuteBars.get(minuteBars.size() - 1), ConfigManager.getInstance().getAcceptablePriceDropPercent(recentBuyFillStatus.getFillPrice(), recentBuyFillStatus.getTicker())))) {
            return true;
        }

        return false;
    }
}
