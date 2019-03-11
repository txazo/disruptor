package com.lmax.disruptor.test;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class MessageEventProducer {

    private final RingBuffer<MessageEvent> ringBuffer;
    private final EventTranslatorOneArg<MessageEvent, String> eventTranslator = new MessageEventTranslator();

    public MessageEventProducer(RingBuffer<MessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String message) {
        ringBuffer.publishEvent(eventTranslator, message);
    }

    private static class MessageEventTranslator implements EventTranslatorOneArg<MessageEvent, String> {

        @Override
        public void translateTo(MessageEvent event, long sequence, String message) {
            event.setMessage(message);
        }

    }

}
