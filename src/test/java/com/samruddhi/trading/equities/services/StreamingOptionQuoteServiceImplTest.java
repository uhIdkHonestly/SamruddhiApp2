package com.samruddhi.trading.equities.services;

import com.samruddhi.trading.equities.domain.OptionData;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class StreamingOptionQuoteServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(StreamingOptionQuoteServiceImplTest.class);

    @Test
    @Ignore
    public void testGetOptionQuote() {
        try {
            StreamingOptionQuoteServiceImpl streamingOptionQuoteService = new StreamingOptionQuoteServiceImpl();
            //OptionData optionData = streamingOptionQuoteService.getOptionQuote("AAPL 240308C167.5");
            OptionData optionData = streamingOptionQuoteService.getOptionQuote("NVDA 240308C885");

            //assertEquals(optionData.getAsk(), "100");
            logger.info(optionData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not expected exception");
        }
    }

    public static void main(String[] args) {
        StreamingOptionQuoteServiceImplTest test = new StreamingOptionQuoteServiceImplTest();
        test.testGetOptionQuote();
    }
}
