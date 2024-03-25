package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStockOrderProcessorImpl {
    private static final Logger logger = LoggerFactory.getLogger(TestStockOrderProcessorImpl.class);

    @Test
    public void initiateStockBuying() {
        try {
            MarketDataService marketDataService = new MarketDataServiceImpl();

            String ticker = "AMZN";
            double lastBarClosePrice = 178.68;
            StockTradeWorker tradeWorker = new StockTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateStockBuying(ticker, lastBarClosePrice);
            String orderId = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void initiateStockSelling() {
        try {
            String ticker = "AMZN";
            double lastBarClosePrice = 178.68;

            MarketDataService marketDataService = new MarketDataServiceImpl();

            StockTradeWorker tradeWorker = new StockTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateStockSelling(ticker, lastBarClosePrice);
            String orderId = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
