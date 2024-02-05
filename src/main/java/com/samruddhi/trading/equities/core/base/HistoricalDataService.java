package com.samruddhi.trading.equities.core.base;

import java.io.IOException;

public interface HistoricalDataService {
    String getHistoricalData(String accessToken) throws IOException;
}
