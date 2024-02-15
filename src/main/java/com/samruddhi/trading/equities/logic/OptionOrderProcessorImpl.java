package com.samruddhi.trading.equities.logic;


import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.GetOrdersByOrderIdResponse;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    private static final int MIN_CALL_QUANTITY  = 2;
    private static final int MIN_PUT_QUANTITY  = 2;
    private static final double BID_ASK_MULTIPLIER  = 0.7;
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
    public void processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
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
        //getOrdersByOrderIdResponse.

    }

    private String getOrderId(PlaceOrderResponse placeOrderResponse, NextStrikePrice nextStrikePrice, String ticker) throws CallOrderException {
        //

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
    public void processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {

    }

    private double getCallBuyPrice(OptionData optionData) {
        return (optionData.getAsk() - optionData.getBid())  * BID_ASK_MULTIPLIER ;
    }
}
