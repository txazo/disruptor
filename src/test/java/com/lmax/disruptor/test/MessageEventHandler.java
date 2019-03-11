package com.lmax.disruptor.test;

import com.lmax.disruptor.EventHandler;

/**
 * 事件消费者
 */
public class MessageEventHandler implements EventHandler<MessageEvent> {

    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(String.format("[%s] onEvent %s", Thread.currentThread().getName(), event));
    }

}
