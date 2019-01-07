package com.eg.tracker.amqp;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("mock-traffic")
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        log.info("Received <" + message + ">");
        this.latch.countDown();
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }

    public void setLatch(CountDownLatch latch) {
    	this.latch = latch;
    }

}
