package core;

import static core.TradingEngine.getInstance;

import com.samruddhi.trading.equities.services.MarketDataServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is entrypoint into the daily trading Application, this can be started as a Stand Alone or be
 * scheduled via Quartz class @link TradingAppScheduler
 */
public class TradingApp implements Job {

    private static final Logger logger = LoggerFactory.getLogger(TradingApp.class);

    public static boolean shutdownNow = false;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.info("{} Entering TradingApp - ", this.getClass().getName());
            // Your job logic here
            getInstance().startEngine();
            logger.info("{} Started TradingApp successfully - ", this.getClass().getName());

        } catch (Exception e) {
            logger.error("JobExecutionException at startup", e.getMessage());
            throw new JobExecutionException(e);
        } finally {
            shutdownHook();
        }
    }

    private void shutdownHook() {

        // Keep job alive until a condition is met (e.g., time-based)
        while (!shutdownNow) {
            try {
                Thread.sleep(2 * 60000); // Sleep for 2 minute
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean isShutdownNow() {
        return shutdownNow;
    }

    public static void setShutdownNow(boolean shutdownNow) {
        TradingApp.shutdownNow = shutdownNow;
    }

    public static void main(String[] args) throws Exception{
        TradingApp tradingApp = new TradingApp();
        tradingApp.execute(null);
        tradingApp.shutdownHook();
    }
}
