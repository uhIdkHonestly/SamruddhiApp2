package com.samruddhi.trading.equities.core.base;

import com.samruddhi.trading.equities.domain.Bar;

import java.util.List;

public interface MarketDataService {

    public List<Bar> getStockDataBars(String ticker) throws Exception;
}
