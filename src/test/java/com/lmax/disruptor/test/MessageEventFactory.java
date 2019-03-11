package com.lmax.disruptor.test;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂
 */
public class MessageEventFactory implements EventFactory<MessageEvent> {

    @Override
    public MessageEvent newInstance() {
        return new MessageEvent();
    }

}
