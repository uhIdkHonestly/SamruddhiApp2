package com.samruddhi.trading.equities.logic;

import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_FILLED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
import com.samruddhi.trading.equities.domain.getordersbyid.Leg;
import com.samruddhi.trading.equities.domain.getordersbyid.Order;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.exceptions.CallOrderException;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.orderlimits.ContractMaxPrice;
import com.samruddhi.trading.equities.services.GetOrdersByOrderIdServiceImpl;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.GetOrdersByOrderIdService;
import com.samruddhi.trading.equities.services.base.OrderService;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;

import com.samruddhi.trading.equities.domain.placeorder.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OptionOrderProcessorImpl.class);
    private static final String CALL_BUY_ORDER = "BUY";
    private static final String CALL_SELL_ORDER = "SELL";

    private static final OrderFillStatus ORDER_FILLSTATUS_FAILED = new OrderFillStatus("0", ORDER_STATUS_FAILED, 0.0, 0, "");

    private static final int MIN_CALL_QUANTITY = 2;
    private static final int MIN_PUT_QUANTITY = 2;
    private static final double BID_ASK_MULTIPLIER = 0.7;

    private static final int ORDER_FILL_RESPONSE_CHECK_TRIES = 3;
    private static final int ORDER_FILL_RESPONSE_WAIT_TIME = 30; // In millis

    private final StreamingOptionQuoteService streamingOptionQuoteService;

    private final OrderService orderService;
    private final GetOrdersByOrderIdService getOrdersByOrderIdService;

    private int NUMBER_OF_RETRIES = 3;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();

        orderService = new OrderServiceImpl();
        getOrdersByOrderIdService = new GetOrdersByOrderIdServiceImpl();
    }

    @Override
    public OrderFillStatus processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("Call process Call Buy Order for {}", nextStrikePrice);
        // Place call BUY order
        return processCallOrder(CALL_BUY_ORDER, nextStrikePrice, ticker, price);
    }

    public OrderFillStatus processCallSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("Call process Call Sell Order for {}", nextStrikePrice);
        // Place call BUY order
        return processCallOrder(CALL_SELL_ORDER, nextStrikePrice, ticker, price);
    }

    /**Place CALL buy Or Sell order
     *
     * @param buyOrSellAction Indicates if we are Buying or Selling
     * @param nextStrikePrice
     * @param ticker
     * @param price
     * @return
     * @throws Exception
     */

    private OrderFillStatus processCallOrder(String buyOrSellAction, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        // Place call BUY
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(ticker, nextStrikePrice.getDateWithStrike());

        // Check if below allowed max for Option contract
        if(!ContractMaxPrice.validateMaxContractPriceByTicker(ticker, optionData.getMid(), price))
            return OrderFillStatus.ORDER_FILL_STATUS_ABORTED;

        double callLimitPrice = getCallOrderPlacementPrice(optionData);

        PlaceOrderPayload payload = new PlaceOrderPayload();
        String encodedOptionTicker = URLEncoder.encode(ticker + " " + nextStrikePrice.getDateWithStrike(), StandardCharsets.UTF_8.toString());

        payload.setAccountID(ConfigManager.getInstance().getProperty("account.id"));
        payload.setSymbol(encodedOptionTicker);
        payload.setQuantity(MIN_CALL_QUANTITY);
        payload.setTradeAction(buyOrSellAction);
        payload.setLimitPrice(callLimitPrice);

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(payload);

        String orderId = getOrderId(placeOrderResponse, nextStrikePrice, ticker);

        OrderFillStatus orderFillStatus = waitForOrderFill(orderId);
        return orderFillStatus;
    }

    @Override
    public OrderFillStatus processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {
        // TO DO Just like call order
        int i = 0;
        while (i < ORDER_FILL_RESPONSE_CHECK_TRIES) {

        }
        return null;
    }

    public OrderFillStatus processPutSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        // TO DO  Implement me
        return null;
    }

    // TO DO
    public OrderFillStatus cancelOrder(long orderId) throws Exception {
        return null;
    }

    private OrderFillStatus waitForOrderFill(String orderId) throws Exception {

        int i = 0;
        while (i < ORDER_FILL_RESPONSE_CHECK_TRIES) {
            OrderFillStatus orderFillStatus = checkOrderFillStatus(orderId);
            if (orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_FAILED || orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_FILLED)
                return orderFillStatus;
            else if (orderFillStatus.getStatus() == OptionOrderFillStatus.ORDER_STATUS_OPEN) {
                logger.info("Order is still open , checking after a few milliseconds");
                Thread.sleep(ORDER_FILL_RESPONSE_WAIT_TIME);
            }
        }
        return new OrderFillStatus("0", "FAILED", 0.0, 0, "");
    }

    private OrderFillStatus checkOrderFillStatus(String orderId) throws Exception {
        GetOrdersByOrderIdResponse getOrdersByOrderIdResponse = getOrdersByOrderIdService.getOrders(orderId);
        if (getOrdersByOrderIdResponse.getErrors() != null && getOrdersByOrderIdResponse.getErrors().size() > 0) {
            return ORDER_FILLSTATUS_FAILED;
        } else if (getOrdersByOrderIdResponse.getOrders() != null && getOrdersByOrderIdResponse.getOrders().size() > 0) {
            Order order = getOrdersByOrderIdResponse.getOrders().get(0);
            // TO DO - Need to check and throw exception if no legs
            Leg leg = order.getLegs().get(0);
            if (order.getStatus().equals(ORDER_STATUS_FILLED)) {
                return new OrderFillStatus(orderId, ORDER_STATUS_FILLED, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol());
            } else if (order.getStatus().equals(ORDER_STATUS_OPEN)) {
                return new OrderFillStatus(orderId, ORDER_STATUS_OPEN, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol());
            }
        }
        return ORDER_FILLSTATUS_FAILED;
    }

    private String getOrderId(PlaceOrderResponse placeOrderResponse, NextStrikePrice nextStrikePrice, String ticker) throws CallOrderException {
        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getErrors().size() > 0) {
            Error error = placeOrderResponse.getErrors().get(0);
            throw new CallOrderException("Error in call order :" + error.getMessage());
        }

        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getOrders().size() > 0) {
            return placeOrderResponse.getOrders().get(0).getOrderID();
        }
        throw new CallOrderException(String.format("Missing or invalid order Id : for ticker %s ", nextStrikePrice.getFullOptionTicker()));
    }

    private double getCallOrderPlacementPrice(OptionData optionData) {
        return (optionData.getAsk() - optionData.getBid()) * BID_ASK_MULTIPLIER;
    }

    public void cancelOrder(String orderId) throws Exception {
        orderService.cancelOrder(orderId);
    }
}
