package com.samruddhi.trading.equities.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ShutdownJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            context.getScheduler().shutdown();
        } catch (SchedulerException e) {
            throw new JobExecutionException("Error shutting down scheduler", e);
        }
    }
}