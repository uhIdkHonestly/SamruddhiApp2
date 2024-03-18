package com.samruddhi.trading.equities.quartz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LowPriorityWriteTaskExecutor {


    private boolean isTerminated = false;

    private final CompletedTaskFileIO completedTaskFileIO;
    public LowPriorityWriteTaskExecutor() {
        completedTaskFileIO = new  CompletedTaskFileIO();
    }
    public  void initiateThreadWorker() {
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
            System.out.println("Executing low-priority task in ExecutorService.");
            // Task logic here, e.g., write to a file or database
        });

        // Don't forget to shut down the executor when it's no longer needed
        executor.shutdown();
    }

    private void saveActionLoop() {
        while(!isTerminated) {

        }
    }

    private void saveToFile() {

    }
}
