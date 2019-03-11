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
                ProducerType.SINGLE, new BlockingWaitStrategy()
        );
        disruptor.handleEventsWith(new MessageEventHandler());
        RingBuffer<MessageEvent> ringBuffer = disruptor.start();
        MessageEventProducer producer = new MessageEventProducer(ringBuffer);
        for (int i = 0; i < 1000000; i++) {
            producer.onData(String.valueOf(i));
        }
    }

}
