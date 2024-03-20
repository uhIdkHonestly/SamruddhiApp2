package com.samruddhi.trading.equities.dummy;

import com.samruddhi.trading.equities.quartz.ShutdownJob;
import core.TradingApp;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;

public class DummyTradingAppScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TradingApp.class);

    public static void scheduleJob() throws SchedulerException {
        logger.info("In DummyTradingAppScheduler");
        JobDetail job = JobBuilder.newJob(DummyTradingApp.class)
                .withIdentity("tradingApp", "group1")
                .build();

        // Trigger the job to run at 9 AM EST
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("tradingAppTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(2, 51)
                        .inTimeZone(TimeZone.getTimeZone("America/New_York")))
                .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

        // Schedule a shutdown at 4 PM EST
        scheduler.scheduleJob(JobBuilder.newJob(ShutdownJob.class)
                        .withIdentity("shutdownJob", "group1").build(),
                TriggerBuilder.newTrigger()
                        .withIdentity("shutdownTrigger", "group1")
                        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16, 0)
                                .inTimeZone(TimeZone.getTimeZone("America/New_York")))
                        .build());
    }

    public static void main(String[] args) throws Exception {
        scheduleJob();
    }
}
