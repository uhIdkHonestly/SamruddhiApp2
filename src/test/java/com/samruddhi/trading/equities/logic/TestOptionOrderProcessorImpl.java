package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestOptionOrderProcessorImpl {

    private static final Logger logger = LoggerFactory.getLogger(TestOptionOrderProcessorImpl.class);

    @Test
    public void initiateCallOrPutBuying() {
        try {
            NextStrikePrice nextStrikePrice = null;
            String ticker = "PLTR";
            double price = 24.01;
            char callOrPut = 'C';

            MarketDataService marketDataService = new MarketDataServiceImpl();
            OptionsTradeWorker tradeWorker = new OptionsTradeWorker(marketDataService, "PLTR");
            tradeWorker.initiateCallOrPutBuying(ticker, price, callOrPut);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
