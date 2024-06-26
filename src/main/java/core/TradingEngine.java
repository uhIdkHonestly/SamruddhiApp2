package core;

import com.samruddhi.trading.equities.domain.Ticker;
import com.samruddhi.trading.equities.domain.TradeWorkerStatus;
import com.samruddhi.trading.equities.logic.base.BaseTradeWorker;
import com.samruddhi.trading.equities.logic.OptionsTradeWorker;
import com.samruddhi.trading.equities.logic.StockTradeWorker;
import com.samruddhi.trading.equities.quartz.TokenRefresherDaemon;
import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TradingEngine {

    private static final Logger logger = LoggerFactory.getLogger(TradingEngine.class);

    private static Integer MAX_THREADS = 20;
    private static TradingEngine instance = null;
    private ExecutorService executor;

    // Private constructor to prevent instantiation from other classes
    private TradingEngine() {
        // Initialize the trading engine here
    }

    // Public static method to get the instance of the Singleton
    public static TradingEngine getInstance() {
        if (instance == null) {
            synchronized (TradingEngine.class) {
                if (instance == null) {
                    instance = new TradingEngine();
                }
            }
        }
        return instance;
    }

    // Add trading-related methods and logic here
    public List<Future<TradeWorkerStatus>> startEngine() {
        List<Future<TradeWorkerStatus>> tradeWorkerFutures = Collections.emptyList();
        try {
            // Start Tradestaion access-token refresher
            TokenRefresherDaemon daemon = new TokenRefresherDaemon();
            daemon.scheduleTokenRefresher();

            // get ticker master
            TickertMaster tickertMaster = new TickertMaster();
            Set<Ticker> tickers = tickertMaster.getTickersForTheDay();

            executor = Executors.newFixedThreadPool(Math.min(tickers.size(), MAX_THREADS));
            List<BaseTradeWorker> workers = tickers.stream().map(ticker -> {
                        if (ticker.isTradeUsingOnlyStocks())
                            return new StockTradeWorker(new MarketDataServiceImpl(), ticker.getName());
                        else
                            return new OptionsTradeWorker(new MarketDataServiceImpl(), ticker.getName());
                    }
            ).collect(Collectors.toList());


        tradeWorkerFutures = executor.invokeAll(workers);

    } catch(
    InterruptedException e)

    {
        logger.error("Error starting TradeEngine {}", e.getMessage());
        logger.error(e.getStackTrace().toString());
    }
        return tradeWorkerFutures;
}
}
