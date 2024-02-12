package com.samruddhi.trading.equities.logic;


import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;

/** Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    StreamingOptionQuoteService streamingOptionQuoteService;
    OptionOrderProcessor optionOrderProcessor;

    private int NUMBER_OF_RETRIES = 3;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        optionOrderProcessor = new OptionOrderProcessorImpl();
    }

    @Override
    public void processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(ticker, nextStrikePrice.getDateWithStrike());
    }

    @Override
    public void processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {

    }
}
