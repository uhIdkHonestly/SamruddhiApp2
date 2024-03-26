package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.Bar;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.domain.placeorder.Error;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.exceptions.CallOrderException;
import com.samruddhi.trading.equities.logic.base.StockOrderProcessor;
import com.samruddhi.trading.equities.services.OrderFillStatusRetrievalService;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.base.MarketDataService;
import com.samruddhi.trading.equities.services.base.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;
import static com.samruddhi.trading.equities.logic.StockQuantityProvider.getStockBuySellQuantityByTicker;

public class StockOrderProcessorImpl implements StockOrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(StockOrderProcessorImpl.class);

    private MarketDataService marketDataService;

    private OrderService orderService;

    private final OrderFillStatusRetrievalService orderFillStatusRetrievalService;

    public StockOrderProcessorImpl(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
        this.orderService = new OrderServiceImpl();
        this.orderFillStatusRetrievalService = new OrderFillStatusRetrievalService();
    }

    @Override
    public OrderFillStatus createStockBuyOrder(String ticker, double price) throws Exception {
        logger.info("entered processCallOrder  for ticker {} initial price {}", ticker, price);
        return buyOrSellOrderInternal(ticker, "BUY", price);
    }

    @Override
    public OrderFillStatus createStockSellOrder(String ticker, double price) throws Exception {
        logger.info("entered createStockSellOrder  for ticker {} initial price {}", ticker, price);

        return buyOrSellOrderInternal(ticker, "SELL", price);
    }


    public void cancelOrder(String orderId) throws Exception {
        orderService.cancelOrder(orderId);
    }

    private OrderFillStatus buyOrSellOrderInternal(String ticker, String buyOrSellAction, double stockPrice) throws Exception {
        List<Bar> minuteBars = marketDataService.getStockDataBars(ticker, "Minute", 1, 2);

        double stockLimitPrice = getLimitOrderPlacementPrice(minuteBars.get(1));

        PlaceOrderPayload payload = new PlaceOrderPayload();
        payload.setOption(false);
        payload.setAccountID(ConfigManager.getInstance().getProperty("account.id"));
        payload.setSymbol(ticker);
        payload.setUnderlyingTicker(ticker);
        payload.setQuantity(getStockBuySellQuantityByTicker(ticker, stockPrice)); // TO DO this needs to vary by Ticker
        payload.setTradeAction(buyOrSellAction);
        payload.setLimitPrice(stockLimitPrice);

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(payload);

        String orderId = getOrderId(placeOrderResponse, ticker);

        OrderFillStatus orderFillStatus = orderFillStatusRetrievalService.waitForOrderFill(orderId);
        orderFillStatus.setPriceOfUnderlying(stockLimitPrice);
        return orderFillStatus;
    }


    private String getOrderId(PlaceOrderResponse placeOrderResponse, String ticker) throws CallOrderException {
        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getErrors().size() > 0) {
            Error error = placeOrderResponse.getErrors().get(0);
            throw new CallOrderException("Error in call order :" + error.getMessage());
        }

        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getOrders().size() > 0) {
            return placeOrderResponse.getOrders().get(0).getOrderId();
        }
        throw new CallOrderException(String.format("Missing or invalid order Id : for ticker %s ", ticker));
    }

    private double getLimitOrderPlacementPrice(Bar bar) {
        return NumberFormatHelper.formatDecimals(bar.getClose(), 2);
    }

    public OrderFillStatus replaceStockSellOrder(String orderId, String ticker) throws Exception {

        OrderFillStatus orderFillStatus = null;
        while (orderFillStatus.getStatus() == null || orderFillStatus.getStatus() == ORDER_STATUS_OPEN) {
            List<Bar> minuteBars = marketDataService.getStockDataBars(ticker, "Minute", 1, 1);
            double limitPrice = NumberFormatHelper.formatDecimals(minuteBars.get(0).getClose(), 2);
            UpdateOrderResponse updateOrderResponse = orderService.updateOrder(StockQuantityProvider.getStockBuySellQuantityByTicker(ticker, limitPrice), orderId, limitPrice);
            orderFillStatus = orderFillStatusRetrievalService.checkOrderFillStatus(orderId);
            if (orderFillStatus == ORDER_FILL_STATUS_FAILED) {
                throw new Exception(String.format("ReplaceCallSellOrder failed  for order %s ticker %s", orderId, ticker));
            }
        }
        // TO DO what if order never fills , we should exit after X minutes
        return orderFillStatus;
    }
}
