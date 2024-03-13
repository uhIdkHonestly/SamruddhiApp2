package com.samruddhi.trading.equities.quartz;

import com.samruddhi.trading.equities.services.TradeStationAuthImpl;
import com.samruddhi.trading.equities.services.base.Authenticator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TradeStation access token expires after every 20 minutes, hence we refresh the token usingb refreshtoken
 * every 18 minutes, so that we always have a working acccess-token
 */
public class TokenRefresherDaemon {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Authenticator authenticator = TradeStationAuthImpl.getInstance();

    public TokenRefresherDaemon() {

    }

    public void scheduleTokenRefresher() {
        scheduler.scheduleAtFixedRate(authenticator::getRefreshToken, 1, 18, TimeUnit.MINUTES);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
