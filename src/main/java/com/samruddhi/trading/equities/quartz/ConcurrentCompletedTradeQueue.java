package com.samruddhi.trading.equities.quartz;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentCompletedTradeQueue {

    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    public void addToQueue(String element) {
        // Adds an element to the tail of this queue.
        queue.offer(element);
    }

    public String removeFromQueue() {
        // Retrieves and removes the head of this queue, or returns null if this queue is empty.
        return queue.poll();
    }
}
