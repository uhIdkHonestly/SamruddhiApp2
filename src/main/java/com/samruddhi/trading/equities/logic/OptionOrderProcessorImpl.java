package com.samruddhi.trading.equities.logic;


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

/** Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OptionOrderProcessorImpl.class);

    private static final int MIN_CALL_QUANTITY  = 2;
    private static final int MIN_PUT_QUANTITY  = 2;
    private static final double BID_ASK_MULTIPLIER  = 0.7;

    //move to enum
    private static final String ORDER_STATUS_FILLED  = "FLL";
    private static final String ORDER_STATUS_OPEN =  "OPN";
    private static final String ORDER_STATUS_ACK =  "ACK";
    private static final String ORDER_STATUS_FAILED =  "FAILED";


    private static final int ORDER_FILL_RESPONSE_CHECK_TRIES = 3;
    private static final int ORDER_FILL_RESPONSE_WAIT_TIME = 30; // In millis

    private final StreamingOptionQuoteService streamingOptionQuoteService;
    private final OptionOrderProcessor optionOrderProcessor;
    private final OrderService orderService;
    private final GetOrdersByOrderIdService getOrdersByOrderIdService;



    private int NUMBER_OF_RETRIES = 3;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        optionOrderProcessor = new OptionOrderProcessorImpl();
        orderService = new OrderServiceImpl();
        getOrdersByOrderIdService = new GetOrdersByOrderIdServiceImpl();
    }

    @Override
    public OrderFillStatus processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        // Place call BUY
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(ticker, nextStrikePrice.getDateWithStrike());
        double callLimitPrice = getCallBuyPrice(optionData);

        PlaceOrderPayload payload = new PlaceOrderPayload();
        String encodedOptionTicker  = URLEncoder.encode(ticker + " " + nextStrikePrice.getDateWithStrike(), StandardCharsets.UTF_8.toString());

        payload.setAccountID(ConfigManager.getInstance().getProperty("account.id"));
        payload.setSymbol(encodedOptionTicker);
        payload.setQuantity(MIN_CALL_QUANTITY);
        payload.setLimitPrice(callLimitPrice);

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(payload);
        // Check order status status

        String orderId = getOrderId(placeOrderResponse, nextStrikePrice, ticker);

        GetOrdersByOrderIdResponse getOrdersByOrderIdResponse = getOrdersByOrderIdService.getOrders(orderId);
        if( getOrdersByOrderIdResponse.getErrors() != null && getOrdersByOrderIdResponse.getErrors().size() > 0) {
            // return error
        } else if( getOrdersByOrderIdResponse.getOrders() != null && getOrdersByOrderIdResponse.getOrders().size() > 0) {
            Order order = getOrdersByOrderIdResponse.getOrders().get(0);
            // TO DO need to checkm and throw exception if no legs
            Leg leg = order.getLegs().get(0);
            if( order.getStatus().equals(ORDER_STATUS_FILLED)) {
                return new OrderFillStatus(ORDER_STATUS_FILLED, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol());
            } else {
                // wait for dome more time

            }
        }
        return new OrderFillStatus(ORDER_STATUS_FAILED, 0.0, 0, "");
    }

    private OrderFillStatus waitForOrderFill(String orderId) throws Exception {

        int i = 0;
        while(i < ORDER_FILL_RESPONSE_CHECK_TRIES) {
            OrderFillStatus orderFillStatus = checkOrderFillStatus(orderId);
            if(orderFillStatus.getStatus().equals(ORDER_STATUS_FAILED) || orderFillStatus.getStatus().equals(ORDER_STATUS_FILLED) )
                return orderFillStatus;
            else if(orderFillStatus.getStatus().equals(ORDER_STATUS_OPEN)) {
                logger.info("Order is still open , checking after a few milliseconds");
                Thread.sleep(ORDER_FILL_RESPONSE_WAIT_TIME);
            }
        }
        return new OrderFillStatus(ORDER_STATUS_FAILED, 0.0, 0, "");

    }

    private OrderFillStatus checkOrderFillStatus(String orderId) throws Exception {
        GetOrdersByOrderIdResponse getOrdersByOrderIdResponse = getOrdersByOrderIdService.getOrders(orderId);
        if( getOrdersByOrderIdResponse.getErrors() != null && getOrdersByOrderIdResponse.getErrors().size() > 0) {
            new OrderFillStatus(ORDER_STATUS_FAILED, 0.0, 0, "");
        } else if( getOrdersByOrderIdResponse.getOrders() != null && getOrdersByOrderIdResponse.getOrders().size() > 0) {
            Order order = getOrdersByOrderIdResponse.getOrders().get(0);
            // TO DO need to check and throw exception if no legs
            Leg leg = order.getLegs().get(0);
            if( order.getStatus().equals(ORDER_STATUS_FILLED)) {
                return new OrderFillStatus(ORDER_STATUS_FILLED, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol());
            } else if( order.getStatus().equals(ORDER_STATUS_OPEN)) {
                return new OrderFillStatus(ORDER_STATUS_OPEN, order.getFilledPrice(), leg.getExecQuantity(), leg.getSymbol());
            }
        }
    }

    private String getOrderId(PlaceOrderResponse placeOrderResponse, NextStrikePrice nextStrikePrice, String ticker) throws CallOrderException {

        if(placeOrderResponse.getErrors() != null && placeOrderResponse.getErrors().size() > 0) {
            Error error = placeOrderResponse.getErrors().get(0);
            throw new CallOrderException("Error in call order :" + error.getMessage());
        }

        if(placeOrderResponse.getErrors() != null && placeOrderResponse.getOrders().size() > 0) {
            return placeOrderResponse.getOrders().get(0).getOrderID();
        }
        throw new CallOrderException(String.format("Missing or invalid order Id : for ticker %s ", nextStrikePrice.getFullOptionTicker()));
    }

    @Override
    public OrderFillStatus processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {

        int i=0;
        while(i < ORDER_FILL_RESPONSE_CHECK_TRIES) {

        }
        return null;
    }

    private double getCallBuyPrice(OptionData optionData) {
        return (optionData.getAsk() - optionData.getBid())  * BID_ASK_MULTIPLIER ;
    }
}