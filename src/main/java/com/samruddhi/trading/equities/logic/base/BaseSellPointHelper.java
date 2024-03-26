package com.samruddhi.trading.equities.logic.base;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.logic.TradeWorkerPriceHelper;
import com.samruddhi.trading.equities.studies.EMACalculator;
import com.samruddhi.trading.equities.studies.MACDCalculator;
import com.samruddhi.trading.equities.studies.RSICalculator;

import java.util.List;

public abstract class BaseSellPointHelper {

    public abstract double getAcceptablePriceChangePercent(double fillPrice, String ticker);

    public boolean determineIfStockOrCallSellCriteriaMet(OrderFillStatus recentBuyFillStatus, List<Bar> allBars) throws Exception {
        double ema5 = EMACalculator.calculateEMAs(allBars, 5);
        double ema13 = EMACalculator.calculateEMAs(allBars, 13);
        double ema50 = EMACalculator.calculateEMAs(allBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = allBars.subList(24, allBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP in the future,
        double rsi = RSICalculator.calculateRSI(allBars.subList(36, allBars.size()), 14);

        if ((ema5 < ema50 || ema5 < ema13 || !isMacdBullish) ||
                (TradeWorkerPriceHelper.hasDroppedByGivenPercentage(recentBuyFillStatus, allBars.get(allBars.size() - 1),  getAcceptablePriceChangePercent(recentBuyFillStatus.getPriceOfUnderlying(), recentBuyFillStatus.getTicker())))) {
            return true;
        }
        return false;
    }

    public boolean determineIfPutSellCriteriaMet(OrderFillStatus recentBuyFillStatus, List<Bar> allBars) throws Exception {
        double ema5 = EMACalculator.calculateEMAs(allBars, 5);
        double ema13 = EMACalculator.calculateEMAs(allBars, 13);
        double ema50 = EMACalculator.calculateEMAs(allBars, 50);

        // calculate and validate MACD
        List<Bar> bars26Days = allBars.subList(24, allBars.size());
        double[] macd = MACDCalculator.computeMACD(bars26Days, 12, 26, 9);
        boolean isMacdBullish = MACDCalculator.isMACDTrendBullish(macd[0], macd[1]);

        // Validate RSI and VWAP,
        double rsi = RSICalculator.calculateRSI(allBars.subList(36, allBars.size()), 14);
        boolean isRsiBullish = rsi > 40; // Fix me

        if ((ema5 > ema50 || ema5 > ema13 || isMacdBullish) ||
                (TradeWorkerPriceHelper.hasIncreasedByGivenPercentage(recentBuyFillStatus, allBars.get(allBars.size() - 1),  getAcceptablePriceChangePercent(recentBuyFillStatus.getPriceOfUnderlying(), recentBuyFillStatus.getTicker())))) {
            return true;
        }

        return false;
    }

}
