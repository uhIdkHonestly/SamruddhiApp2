package com.samruddhi.trading.equities.logic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import org.junit.Test;

public class TestTradeWorkerPriceHelper {
    @Test
    public void stockPriceHasNotDroppedByGivenPercentage() {
        String ticker = "AAPL";
        OrderFillStatus buyFillStatus = new OrderFillStatus();
        buyFillStatus.setFillPrice(201);
        buyFillStatus.setTicker(ticker);
        buyFillStatus.setPriceOfUnderlying(201);
        Bar currentOptionPriceBar = new Bar();
        currentOptionPriceBar.setClose("199");

        StockSellPointHelper stockSellPointHelper = new StockSellPointHelper(ticker);

        boolean hasPriceDroppedBelowThreshhold = TradeWorkerPriceHelper.hasDroppedByGivenPercentage(buyFillStatus, currentOptionPriceBar, stockSellPointHelper.getAcceptablePriceChangePercent(buyFillStatus.getPriceOfUnderlying(), ticker));

        assertFalse(hasPriceDroppedBelowThreshhold);
    }

    @Test
    public void stockPriceHasDroppedByGivenPercentage() {
        String ticker = "AAPL";
        OrderFillStatus buyFillStatus = new OrderFillStatus();
        buyFillStatus.setFillPrice(201);
        buyFillStatus.setPriceOfUnderlying(201);
        buyFillStatus.setTicker(ticker);
        Bar currentOptionPriceBar = new Bar();
        currentOptionPriceBar.setClose("195");

        StockSellPointHelper stockSellPointHelper = new StockSellPointHelper(ticker);

        boolean hasPriceDroppedBelowThreshhold = TradeWorkerPriceHelper.hasDroppedByGivenPercentage(buyFillStatus, currentOptionPriceBar, stockSellPointHelper.getAcceptablePriceChangePercent(buyFillStatus.getPriceOfUnderlying(), ticker));

        assertTrue(hasPriceDroppedBelowThreshhold);
    }

    @Test
    public void stockPriceHasIncreasedByGivenPercentage() {
        String ticker = "NET";
        OrderFillStatus buyFillStatus = new OrderFillStatus();
        buyFillStatus.setFillPrice(120);
        buyFillStatus.setPriceOfUnderlying(120);
        buyFillStatus.setTicker(ticker);
        Bar currentOptionPriceBar = new Bar();
        currentOptionPriceBar.setClose("125");

        StockSellPointHelper stockSellPointHelper = new StockSellPointHelper(ticker);

        boolean hasPriceDroppedBelowThreshhold = TradeWorkerPriceHelper.hasIncreasedByGivenPercentage(buyFillStatus, currentOptionPriceBar, stockSellPointHelper.getAcceptablePriceChangePercent(buyFillStatus.getPriceOfUnderlying(), ticker));

        assertTrue(hasPriceDroppedBelowThreshhold);
    }

    @Test
    public void tinyStockPriceHasDroppedByGivenPercentage() {
        OrderFillStatus buyFillStatus = new OrderFillStatus();
        buyFillStatus.setFillPrice(27.1);
        buyFillStatus.setTicker("PLTR");
        Bar currentOptionPriceBar = new Bar();
        currentOptionPriceBar.setClose("26.27");

        double threshHoldPercentage = 0.03;

        boolean hasPriceDroppedBelowThreshhold = TradeWorkerPriceHelper.hasDroppedByGivenPercentage(buyFillStatus, currentOptionPriceBar, threshHoldPercentage);

        assertTrue(hasPriceDroppedBelowThreshhold);
    }


    @Test
    public void stockOptionCallHasNotDroppedByGivenPercentage() {
        OrderFillStatus buyFillStatus = new OrderFillStatus();
        buyFillStatus.setFillPrice(2.15);
        buyFillStatus.setTicker("AAPL 240328C180");
        Bar currentOptionPriceBar = new Bar();
        currentOptionPriceBar.setClose("198");

        double threshHoldPercentage = 0.02;

        boolean hasPriceDroppedBelowThreshhold = TradeWorkerPriceHelper.hasDroppedByGivenPercentage(buyFillStatus, currentOptionPriceBar, threshHoldPercentage);

        assertFalse(hasPriceDroppedBelowThreshhold);
    }
}
