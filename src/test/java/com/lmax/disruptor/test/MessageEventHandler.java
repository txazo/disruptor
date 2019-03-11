package com.lmax.disruptor.test;

import com.lmax.disruptor.EventHandler;

/**
 * 事件处理
 */
public class MessageEventHandler implements EventHandler<MessageEvent> {

    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Event: " + event);
    }

}
