package com.lmax.disruptor.test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 */
public class MessageThreadFactory implements ThreadFactory {

    private AtomicInteger incr = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "disruptor-thread-" + incr.incrementAndGet());
    }

}
