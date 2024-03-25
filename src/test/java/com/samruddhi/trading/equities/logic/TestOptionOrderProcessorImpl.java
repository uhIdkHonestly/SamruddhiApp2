package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.orderlimits.OptionTickerProvider;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestOptionOrderProcessorImpl {

    private static final Logger logger = LoggerFactory.getLogger(TestOptionOrderProcessorImpl.class);

    @Test
    public void initiateCallBuying() {
        try {
            NextStrikePrice nextStrikePrice = null;
            String ticker = "PLTR";
            double price = 24.01;
            char callOrPut = 'C';

            MarketDataService marketDataService = new MarketDataServiceImpl();
            OptionsTradeWorker tradeWorker = new OptionsTradeWorker(marketDataService, ticker);
            tradeWorker.initiateCallOrPutBuying(ticker, price, callOrPut);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initiatePutBuying() {
        try {
            NextStrikePrice nextStrikePrice = null;
            String ticker = "AAPL";
            double price = 172.16;
            char callOrPut = 'P';

            MarketDataService marketDataService = new MarketDataServiceImpl();
            OptionsTradeWorker tradeWorker = new OptionsTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateCallOrPutBuying(ticker, price, callOrPut);
            String orderId  = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initiateCallSelling() {
        try {
            String ticker = "AMZN";
            double lastBarClosePrice = 178.8;
            char callOrPut = 'C';
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, lastBarClosePrice, callOrPut);
            MarketDataService marketDataService = new MarketDataServiceImpl();
            OptionsTradeWorker tradeWorker = new OptionsTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateCallOrPutSelling(nextStrikePrice, ticker, lastBarClosePrice, callOrPut);
            String orderId  = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initiatePutSelling() {
        try {
            String ticker = "AMZN";
            double lastBarClosePrice = 178.68;
            char callOrPut = 'P';
            NextStrikePrice nextStrikePrice = OptionTickerProvider.getNextOptionTicker(ticker, lastBarClosePrice, callOrPut);
            MarketDataService marketDataService = new MarketDataServiceImpl();
            OptionsTradeWorker tradeWorker = new OptionsTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateCallOrPutSelling(nextStrikePrice, ticker, lastBarClosePrice, callOrPut);
            String orderId  = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}