package com.samruddhi.trading.equities.logic;


import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.PlaceOrderPayload;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.OrderService;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;

/** Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    private static final double BID_ASK_MULTIPLIER  = 0.7;
    private final StreamingOptionQuoteService streamingOptionQuoteService;
    private final OptionOrderProcessor optionOrderProcessor;
    private final OrderService orderService;

    private int NUMBER_OF_RETRIES = 3;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        optionOrderProcessor = new OptionOrderProcessorImpl();
        orderService = new OrderServiceImpl();
    }

    @Override
    public void processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(ticker, nextStrikePrice.getDateWithStrike());
        double callLimitPrice = getCallBuyPrice(optionData);

        //PlaceOrderPayload payload = new PlaceOrderPayload()
        //orderService.placeOrder(payload);



    }

    @Override
    public void processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {

    }

    private double getCallBuyPrice(OptionData optionData) {
        return (optionData.getAsk() - optionData.getBid())  * BID_ASK_MULTIPLIER ;
    }
}
