package com.lmax.disruptor.test;

/**
 * 事件
 */
public class MessageEvent {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageEvent{" + "message='" + message + '\'' + '}';
    }

}
