package com.demo.core;

import com.demo.model.Event;
import com.demo.sender.EventSender;
import com.demo.exception.ValidationException;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class EventDispatcher {
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private final EventValidator validator;
    private final EventSender sender;

    public final LongAdder totalReceived = new LongAdder();
    public final LongAdder successCount = new LongAdder();
    public final LongAdder sendFailCount = new LongAdder();
    public final LongAdder validateFailCount = new LongAdder();

    public final ConcurrentHashMap<String, AtomicInteger> processTracker = new ConcurrentHashMap<>();

    public EventDispatcher(EventValidator validator, EventSender sender) {
        this.validator = validator;
        this.sender = sender;
    }

    public void dispatch(Event event) {
        totalReceived.increment();
        executor.submit(() -> process(event));
    }

    private void process(Event event) {
        processTracker.computeIfAbsent(event.getEventId(), k -> new AtomicInteger(0)).incrementAndGet();

        try {
            validator.validateAll(event);
            if (sender.send(event)) {
                successCount.increment();
            } else {
                sendFailCount.increment();
            }
        } catch (ValidationException e) {
            validateFailCount.increment();
        } catch (Exception e) {
            sendFailCount.increment();
        }
    }

    public void shutdown() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }
}