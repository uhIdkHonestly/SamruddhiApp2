package com.samruddhi.trading.equities.quartz;

import org.junit.Test;

public class TokenRefresherDaemonTest {

    @Test
    public void testScheduleTokenRefresher() {
        TokenRefresherDaemon tokenRefresherDaemon = new TokenRefresherDaemon();
        tokenRefresherDaemon.setRefreshIntervalMins(1);
        tokenRefresherDaemon.scheduleTokenRefresher();

        try {
            Thread.sleep(60 * 1000 * 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
