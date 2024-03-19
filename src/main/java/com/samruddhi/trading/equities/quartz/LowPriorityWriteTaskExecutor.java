package com.samruddhi.trading.equities.quartz;

import com.samruddhi.trading.equities.services.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LowPriorityWriteTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private boolean isTerminated = false;

    private final CompletedTaskFileIO completedTaskFileIO;

    private final ConcurrentCompletedTradeQueue concurrentCompletedTradeQueue;

    public LowPriorityWriteTaskExecutor() {
        this.completedTaskFileIO = new  CompletedTaskFileIO();
        this.concurrentCompletedTradeQueue = ConcurrentCompletedTradeQueue.getInstance();
    }



    public  void initiateThreadWorker() throws InterruptedException, IOException{
        // Create an ExecutorService with a custom thread factory for low-priority threads
        ExecutorService executor = Executors.newFixedThreadPool(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MIN_PRIORITY); // Set thread to low priority
                return thread;
            }
        });

        // Submit tasks to the executor service
        executor.submit(() -> {

            try {
                logger.info("Executing low-priority task in ExecutorService.");
                // Task logic here, e.g., write to a file or database
                saveActionLoop();
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Error in LowPriorityWriteTaskExecutor -> saveActionLoop");
            }
        });

        // Don't forget to shut down the executor when it's no longer needed
        executor.shutdown();
    }

    private void saveActionLoop() throws IOException, InterruptedException {
        while(!isTerminated) {
           String completedTrade =  concurrentCompletedTradeQueue.removeFromQueue();
           if(completedTrade != null) {
               completedTaskFileIO.writeRow(completedTrade);
           }
           Thread.sleep(200);
        }
    }

    private void saveToFile() {

    }
}
