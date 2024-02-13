package com.samruddhi.trading.equities.logic;


import com.samruddhi.trading.equities.config.ConfigManager;
import com.samruddhi.trading.equities.domain.NextStrikePrice;
import com.samruddhi.trading.equities.domain.OptionData;
import com.samruddhi.trading.equities.domain.Order;
import com.samruddhi.trading.equities.logic.base.OptionOrderProcessor;
import com.samruddhi.trading.equities.services.OrderServiceImpl;
import com.samruddhi.trading.equities.services.StreamingOptionQuoteServiceImpl;
import com.samruddhi.trading.equities.services.base.OrderService;
import com.samruddhi.trading.equities.services.base.StreamingOptionQuoteService;

/** Intelligently process Option order, retrieve current bid/ask each time, this should include retry logic as Option price may have gone up or down after Order is placed first
 * */
public class OptionOrderProcessorImpl implements OptionOrderProcessor {

    private final static String ACCOUNT_ID_KEY = "account.id";
    private final StreamingOptionQuoteService streamingOptionQuoteService;
    private final OrderService orderService;

    private int NUMBER_OF_RETRIES = 3;

    public OptionOrderProcessorImpl() {
        streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
        orderService = new OrderServiceImpl();
    }

    @Override
    public void processCallBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {
        OptionData optionData = streamingOptionQuoteService.getOptionQuote(ticker, nextStrikePrice.getDateWithStrike());


        Order order = new Order();
        order.setAccountID(ConfigManager.getInstance().getProperty(ACCOUNT_ID_KEY));
        //order.setLimitPrice(ConfigManager.getInstance().getProperty(ACCOUNT_ID_KEY));
        try {
            orderService.placeOrder(order);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double determineLimitPrice(OptionData optionData) {
        return 0.0;
    }

    @Override
    public void processPutBuyOrder(NextStrikePrice nextStrikePrice, String ticker, double price) {

    }
}
