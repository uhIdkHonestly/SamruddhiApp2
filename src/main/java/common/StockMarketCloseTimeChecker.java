package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class StockMarketCloseTimeChecker {

    private static final Logger logger = LoggerFactory.getLogger(StockMarketCloseTimeChecker.class);

    ZoneId zoneIdEST = ZoneId.of("America/New_York");
    ZoneId zoneIdPST = ZoneId.of("America/Los_Angeles");

    /**
     * if time is at on after 3.45 we are close to stock market close time at 4PM. Hence we must close all
     * open Stock and Option positions for the day
     *
     * @return true if currrent time in EST is past 3.45 PM
     */
    public boolean isCloseToMarketCloseTime() {
        // Current time in PST
        ZonedDateTime currentTimePST = ZonedDateTime.now(zoneIdPST);
        logger.debug("Current time in PST: " + currentTimePST);

        // Convert PST time to EST
        ZonedDateTime currentTimeEST = currentTimePST.withZoneSameInstant(zoneIdEST);
        logger.debug("Current time in EST: " + currentTimeEST);

        // Check if current EST time is past 3:45 PM
        LocalTime checkTime = LocalTime.of(15, 45); // 3:45 PM
        boolean isPastThreeFortyFive = currentTimeEST.toLocalTime().isAfter(checkTime);

        if (isPastThreeFortyFive) {
            logger.info("The current time {} in EST is past 3:45 PM., hence we need to initiate closing of ALL open Stock and Option positions for the day", currentTimeEST.toLocalTime());
            return true;
        } else {
            logger.debug("The current time in EST is not past 3:45 PM.");
            return false;
        }
    }
}
