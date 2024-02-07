package core;

import com.samruddhi.trading.equities.domain.Ticker;

import java.util.Set;

import static com.samruddhi.trading.equities.domain.OptionType.Daily;
import static com.samruddhi.trading.equities.domain.OptionType.Weekly;

/**
 * get All tickers neededd for the day, essentilaly gets stock or etf tickets , then Option tickers are derived by the undelying by the code
 * ie Daily or Weekly options (puts or calls)
 * TO DO We need to pull this infromation from a Database or NoSql
 */
public class TickertMaster {
    private final Set<Ticker> coreTickerSet = Set.of(new Ticker("SPY", Daily), new Ticker("QQQ", Daily), new Ticker("TQQQ", Weekly),
            new Ticker("NVDA", Weekly), new Ticker("AMD", Weekly), new Ticker("AMZN", Weekly), new Ticker("GOOGL", Weekly),
            new Ticker("MARA", Weekly), new Ticker("TSLA", Weekly));

    public Set<Ticker> getTickersForTheDay() {
        return coreTickerSet;
    }
}