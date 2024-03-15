package com.samruddhi.trading.equities.quartz;

import org.junit.Test;

public class TokenRefresherDaemonTest {

    @Test
    public void testScheduleTokenRefresher() {
        TokenRefresherDaemon tokenRefresherDaemon = new TokenRefresherDaemon();
        tokenRefresherDaemon.setRefreshIntervalMins(2);
        tokenRefresherDaemon.scheduleTokenRefresher();
    }
}
