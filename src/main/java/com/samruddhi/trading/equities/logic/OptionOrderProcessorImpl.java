package com.samruddhi.trading.equities.logic;

import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus;
import com.samruddhi.trading.equities.domain.placeorder.Error;
import com.samruddhi.trading.equities.domain.placeorder.PlaceOrderResponse;
import com.samruddhi.trading.equities.domain.updateorder.UpdateOrderResponse;
import com.samruddhi.trading.equities.exceptions.CallOrderException;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.orderlimits.ContractMaxPrice;
import com.samruddhi.trading.equities.services.OrderFillStatusRetrievalService;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.OrderService;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;
import common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.samruddhi.trading.equities.domain.getordersbyid.OrderFillStatus.ORDER_FILL_STATUS_FAILED;
import static com.samruddhi.trading.equities.logic.OptionOrderFillStatus.ORDER_STATUS_OPEN;

/**
 * Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OptionOrderProcessorImpl.class);

    private static final String BUY_ORDER = "BUY";
    private static final String CALL_BUY_ORDER = "BUY";
    private static final String CALL_SELL_ORDER = "SELL";

    private static final String PUT_BUY_ORDER = "BUY";
    private static final String PUT_SELL_ORDER = "SELL";

    private static final int MIN_CALL_QUANTITY = 2;
    private static final int MIN_PUT_QUANTITY = 2;
    private static final double BID_ASK_MULTIPLIER = 0.7;

    private final StreamingOptionQuoteService streamingOptionQuoteService;

    private final OrderFillStatusRetrievalService orderFillStatusRetrievalService;

    private final OrderService orderService;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        orderService = new OrderServiceImpl();
        orderFillStatusRetrievalService = new OrderFillStatusRetrievalService();
    }

    @Override
    public OrderFillStatus createCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("createCallBuyOrder Order for {}", nextStrikePrice);
        // Place call BUY order
        return processCallOrder(CALL_BUY_ORDER, nextStrikePrice, ticker, price);
    }

    public OrderFillStatus createCallSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("Call process Call Sell Order for {}", nextStrikePrice);
        return processCallOrder(CALL_SELL_ORDER, nextStrikePrice, ticker, price);
    }

    /**
     * Place CALL buy Or Sell order
     *
     * @param buyOrSellAction Indicates if we are Buying or Selling
     * @param nextStrikePrice
     * @param ticker          Underlying stock
     * @param price
     * @return
     * @throws Exception
     */
    private OrderFillStatus processCallOrder(String buyOrSellAction, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("entered processCallOrder for {} for {} ticker {} price {}", buyOrSellAction, nextStrikePrice, ticker, price);
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(nextStrikePrice.getFullOptionTicker());
        logger.info("optionData quote {}" , optionData);
        // Check if below allowed max for Option contract
        if (buyOrSellAction == BUY_ORDER) {
            if (!ContractMaxPrice.validateMaxContractPriceByTicker(ticker, optionData.getMid(), price)) {
                logger.info("Not buying CALL for ticker {} due to high price {}", nextStrikePrice.getFullOptionTicker(), price);
                return OrderFillStatus.ORDER_FILL_STATUS_ABORTED;
            }
        }

        double callLimitPrice = getCallOrderPlacementPrice(optionData);

        PlaceOrderPayload payload = new PlaceOrderPayload();
        payload.setOption(true);
        //String encodedOptionTicker = CommonUtils.replaceSpaces(nextStrikePrice.getFullOptionTicker());
        String encodedOptionTicker =  nextStrikePrice.getFullOptionTicker();

        payload.setAccountID(ConfigManager.getInstance().getProperty("account.id"));
        payload.setSymbol(encodedOptionTicker);
        payload.setUnderlyingTicker(ticker);
        payload.setQuantity(MIN_CALL_QUANTITY);
        payload.setTradeAction(buyOrSellAction);
        payload.setLimitPrice(callLimitPrice);

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(payload);

        String orderId = getOrderId(placeOrderResponse, nextStrikePrice, ticker);

        OrderFillStatus orderFillStatus = orderFillStatusRetrievalService.waitForOrderFill(orderId);
        return orderFillStatus;
    }



    @Override
    public OrderFillStatus createPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("PUT Buy Order for {}", nextStrikePrice);
        // Place PUT BUY order
        return processPutOrder(PUT_BUY_ORDER, nextStrikePrice, ticker, price);
    }

    public OrderFillStatus createPutSellOrder(NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("PUT Sell Order for {}", nextStrikePrice);
        return processPutOrder(PUT_SELL_ORDER, nextStrikePrice, ticker, price);
    }

    private OrderFillStatus processPutOrder(String buyOrSellAction, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {
        logger.info("entered processPutOrder for {} for {} ticker {} price {}", buyOrSellAction, nextStrikePrice, ticker, price);
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(nextStrikePrice.getFullOptionTicker());

        // Check if below allowed max for Option contract
        if (buyOrSellAction == BUY_ORDER) {
            if (!ContractMaxPrice.validateMaxContractPriceByTicker(ticker, optionData.getMid(), price)) {
                logger.info("Not buying PUT for ticker {} due to high price {}", nextStrikePrice.getFullOptionTicker(), price);
                return OrderFillStatus.ORDER_FILL_STATUS_ABORTED;
            }
        }

        double putLimitPrice = getPutOrderPlacementPrice(optionData);

        PlaceOrderPayload payload = new PlaceOrderPayload();
        payload.setOption(true);
       //String encodedOptionTicker = CommonUtils.replaceSpaces(nextStrikePrice.getFullOptionTicker());
        String encodedOptionTicker = nextStrikePrice.getFullOptionTicker();

        payload.setAccountID(ConfigManager.getInstance().getProperty("account.id"));
        payload.setSymbol(encodedOptionTicker);
        payload.setUnderlyingTicker(ticker);
        payload.setQuantity(MIN_PUT_QUANTITY);
        payload.setTradeAction(buyOrSellAction);
        payload.setLimitPrice(putLimitPrice);

        PlaceOrderResponse placeOrderResponse = orderService.placeOrder(payload);

        String orderId = getOrderId(placeOrderResponse, nextStrikePrice, ticker);

        OrderFillStatus orderFillStatus = orderFillStatusRetrievalService.waitForOrderFill(orderId);
        return orderFillStatus;
    }

    public OrderFillStatus cancelOrder(long orderId) throws Exception {
        // TO DO
        return null;
    }

    private String getOrderId(PlaceOrderResponse placeOrderResponse, NextStrikePrice nextStrikePrice, String ticker) throws CallOrderException {
        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getErrors().size() > 0) {
            Error error = placeOrderResponse.getErrors().get(0);
            throw new CallOrderException("Error in call order :" + error.getMessage());
        }

        if (placeOrderResponse.getErrors() != null && placeOrderResponse.getOrders().size() > 0) {
            return placeOrderResponse.getOrders().get(0).getOrderId();
        }
        throw new CallOrderException(String.format("Missing or invalid order Id : for ticker %s ", nextStrikePrice.getFullOptionTicker()));
    }

    private double getCallOrderPlacementPrice(OptionData optionData) {
        // TO DO We need better logic here
        //return (optionData.getAsk() - optionData.getBid()) * BID_ASK_MULTIPLIER;
        return optionData.getMid();
    }

    private double getPutOrderPlacementPrice(OptionData optionData) {
        //return (optionData.getAsk() - optionData.getBid()) * BID_ASK_MULTIPLIER;
        return optionData.getMid();
    }

    public void cancelOrder(String orderId) throws Exception {
        orderService.cancelOrder(orderId);
    }

    /**
     * this will replace existing CALL or PUT sell order with a new LIMIT price
     *
     * @param orderId
     * @param nextStrikePrice
     * @param ticker
     * @param price
     * @return
     * @throws Exception
     */
    @Override
    public OrderFillStatus replaceCallOrPutSellOrder(String orderId, NextStrikePrice nextStrikePrice, String ticker, double price) throws Exception {

        OrderFillStatus orderFillStatus = null;
        while (orderFillStatus.getStatus() == null || orderFillStatus.getStatus() == ORDER_STATUS_OPEN) {
            OptionData optionData = streamingOptionQuoteService.getOptionQuote(nextStrikePrice.getFullOptionTicker());
            double callLimitPrice = getCallOrderPlacementPrice(optionData);
            UpdateOrderResponse updateOrderResponse = orderService.updateOrder(2, orderId, callLimitPrice);
            orderFillStatus = orderFillStatusRetrievalService.checkOrderFillStatus(orderId);
            if (orderFillStatus == ORDER_FILL_STATUS_FAILED) {
                throw new Exception(String.format("ReplaceCallSellOrder failed  for order %s ticker %s", orderId, ticker));
            }
        }
        // TO DO what if order never fills , we should exit after X minutes
        return orderFillStatus;
    }
}
