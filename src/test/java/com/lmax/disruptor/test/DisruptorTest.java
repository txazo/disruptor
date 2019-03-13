package com.lmax.disruptor.test;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * 生产者: 单生产者、多生产者
 * 消费者: 单消费者、消费者组
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

    // 多生产者多消费者
    @Test
    public void test1() {
        initDisruptor(16, 2, ProducerType.MULTI);
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println(String.format("[%s] EventHandler1 onEvent %s", Thread.currentThread().getName(), event));
            Thread.sleep(500);
        }).then((event, sequence, endOfBatch) -> {
            System.out.println(String.format("[%s] EventHandler2 onEvent %s", Thread.currentThread().getName(), event));
            Thread.sleep(100);
        });
        ringBuffer = disruptor.start();

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    for (int i = 1; i <= 10000; i++) {
                        ringBuffer.publishEvent((event, sequence, message) -> event.setMessage(message), "1-" + String.valueOf(i));
                    }
                }),
                CompletableFuture.runAsync(() -> {
                    for (int i = 1; i <= 10000; i++) {
                        ringBuffer.publishEvent((event, sequence, message) -> event.setMessage(message), "2-" + String.valueOf(i));
                    }
                })
        ).join();
    }

    // 单生产者消费者组
    @Test
    public void test2() {
        initDisruptor(16, 2, ProducerType.SINGLE);
        disruptor.handleEventsWithWorkerPool(
                event -> {
                    System.out.println(String.format("[%s] WorkerPool WorkHandler1 onEvent %s", Thread.currentThread().getName(), event));
                    Thread.sleep(1000);
                },
                event -> {
                    System.out.println(String.format("[%s] WorkerPool WorkHandler2 onEvent %s", Thread.currentThread().getName(), event));
                    Thread.sleep(1000);
                });
        ringBuffer = disruptor.start();

        for (int i = 1; i <= 10000; i++) {
            ringBuffer.publishEvent((event, sequence, message) -> event.setMessage(message), String.valueOf(i));
        }
    }

}
