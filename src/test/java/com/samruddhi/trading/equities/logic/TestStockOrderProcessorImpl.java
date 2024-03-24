package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStockOrderProcessorImpl {
    private static final Logger logger = LoggerFactory.getLogger(TestStockOrderProcessorImpl.class);

    @Test
    public void initiateStockBuying() {
        try {
            String ticker = "PLTR";
            double price = 24.01;

            MarketDataService marketDataService = new MarketDataServiceImpl();
          /*
            StockTradeWorker tradeWorker = new StockTradeWorker(marketDataService, "PLTR");
            tradeWorker.initiateStockBuying( ticker,   price);*/

            ticker = "NVDA";
            price = 942.89;
            StockTradeWorker tradeWorker = new StockTradeWorker(marketDataService, "NVDA");
            tradeWorker.initiateStockBuying(ticker, price);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
