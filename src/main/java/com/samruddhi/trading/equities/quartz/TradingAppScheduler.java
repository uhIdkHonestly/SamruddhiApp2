package com.samruddhi.trading.equities.quartz;

import core.TradingApp;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.TimeZone;

public class TradingAppScheduler {

    public static void scheduleJob() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(TradingApp.class)
                .withIdentity("tradingApp", "group1")
                .build();

        // Trigger the job to run at 9 AM EST
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("tradingAppTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(1, 9)
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

    public static void main(String[] args) throws Exception{
        scheduleJob();
    }

}
