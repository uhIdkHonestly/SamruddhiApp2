package com.samruddhi.trading.equities.services.base;

import java.io.IOException;

public interface HistoricalDataService {
    String getHistoricalData(String accessToken) throws IOException;
}
