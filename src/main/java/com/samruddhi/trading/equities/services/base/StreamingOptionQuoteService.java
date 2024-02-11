package com.samruddhi.trading.equities.services.base;

import com.samruddhi.trading.equities.domain.OptionData;

public interface StreamingOptionQuoteService {
    public OptionData getOptionQuote(String url, String ticker, String optionStrike);
}
