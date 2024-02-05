package core;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/** This is entrypoint into the daily trading Application, this can be started as a Stand Alone or be
 * scheduled via Quartz class @link TradingAppScheduler
  */
public class TradingApp implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Executing My Scheduled Job");
        // Your job logic here
    }
}
