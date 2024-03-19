package com.samruddhi.trading.equities.quartz;

import com.samruddhi.trading.equities.config.ConfigManager;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentCompletedTradeQueue {

    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    private static ConcurrentCompletedTradeQueue instance;

    public void addToQueue(String element) {
        // Adds an element to the tail of this queue.
        queue.offer(element);
    }

    public String removeFromQueue() {
        // Retrieves and removes the head of this queue, or returns null if this queue is empty.
        return queue.poll();
    }

    private ConcurrentCompletedTradeQueue() {

    }

    public static synchronized ConcurrentCompletedTradeQueue getInstance() {
        if (instance == null) {
            instance = new ConcurrentCompletedTradeQueue();
        }
        return instance;
    }
}
