package com.lmax.disruptor.test;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

public class DisruptorTest {

    public static void main(String[] args) {
        Disruptor<MessageEvent> disruptor = new Disruptor<>(
                new MessageEventFactory(), 1024, Executors.newFixedThreadPool(10, new MessageThreadFactory()),
                ProducerType.MULTI, new BlockingWaitStrategy()
        );
        disruptor.handleEventsWith(new MessageEventHandler());
        disruptor.start();

        RingBuffer<MessageEvent> ringBuffer = disruptor.getRingBuffer();
        MessageEventProducer producer = new MessageEventProducer(ringBuffer);
        for (int i = 0; i < 10000; i++) {
            producer.onData(String.valueOf(i));
        }
    }

}
