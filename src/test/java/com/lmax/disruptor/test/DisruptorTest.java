package com.lmax.disruptor.test;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * 生产者: 单生产者、多生产者
 * 消费者: 单消费者、多消费者、消费者群组
 */
public class DisruptorTest {

    private Disruptor<MessageEvent> disruptor;
    private RingBuffer<MessageEvent> ringBuffer;

    private void initDisruptor(int ringBufferSize, int poolSize, ProducerType producerType) {
        disruptor = new Disruptor<>(
                new MessageEventFactory(), ringBufferSize, Executors.newFixedThreadPool(poolSize, new MessageThreadFactory()),
                producerType, new BlockingWaitStrategy()
        );
    }

    @After
    public void close() {
        disruptor.shutdown();
    }

    // 单生产者单消费者
    @Test
    public void test1() {
        initDisruptor(16, 2, ProducerType.SINGLE);
        disruptor.handleEventsWith(
                (event, sequence, endOfBatch) -> {
                    System.out.println(String.format("[%s] EventHandler onEvent %s", Thread.currentThread().getName(), event));
                    Thread.sleep(10);
                }
        );
        ringBuffer = disruptor.start();

        for (int i = 0; i < 100; i++) {
            ringBuffer.publishEvent((event, sequence, message) -> event.setMessage(message), String.valueOf(i));
        }
    }

    // 单生产者多消费者
    @Test
    public void test2() {
        initDisruptor(16, 2, ProducerType.SINGLE);
        disruptor.handleEventsWithWorkerPool(
                event -> {
                    System.out.println(String.format("[%s] EventHandlerGroup EventHandler1 onEvent %s", Thread.currentThread().getName(), event));
                    Thread.sleep(10);
                },
                event -> {
                    System.out.println(String.format("[%s] EventHandlerGroup EventHandler2 onEvent %s", Thread.currentThread().getName(), event));
                    Thread.sleep(10);
                });
        ringBuffer = disruptor.start();

        for (int i = 0; i < 100; i++) {
            ringBuffer.publishEvent((event, sequence, message) -> event.setMessage(message), String.valueOf(i));
        }
    }

}
