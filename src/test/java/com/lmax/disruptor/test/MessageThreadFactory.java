package com.lmax.disruptor.test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 */
public class MessageThreadFactory implements ThreadFactory {

    private AtomicInteger incr = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, String.format("disruptor-executor-%02d", incr.incrementAndGet()));
    }

}
