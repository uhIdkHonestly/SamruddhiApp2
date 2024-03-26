package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import com.samruddhi.trading.equities.services.base.OrderService;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStockOrderProcessorImpl {
    private static final Logger logger = LoggerFactory.getLogger(TestStockOrderProcessorImpl.class);
    private String orderId = "826048629";

    @Test
    public void initiateStockBuying() {
        try {
            MarketDataService marketDataService = new MarketDataServiceImpl();

            String ticker = "AMZN";
            double lastBarClosePrice = 177;
            StockTradeWorker tradeWorker = new StockTradeWorker(marketDataService, ticker);
            OrderFillStatus orderFillStatus = tradeWorker.initiateStockBuying(ticker, lastBarClosePrice);
            orderId = orderFillStatus.getOrderId();
            Assert.assertTrue(!orderId.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void updateOrder() {
        try {

            String ticker = "AMZN";
            double lastBarClosePrice = 179.65;

            OrderService orderService = new OrderServiceImpl();
            UpdateOrderResponse response = orderService.updateOrder(15, orderId, lastBarClosePrice);

            Assert.assertTrue(response.getOrderID().equals(orderId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelOrder() {
        try {
            OrderService orderService = new OrderServiceImpl();
            orderService.cancelOrder(orderId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initiateStockSelling() {
        try {
            String ticker = "AMZN";
            double lastBarClosePrice = 179.68;

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
