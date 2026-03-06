package com.demo;

import com.demo.core.EventDispatcher;
import com.demo.core.EventValidator;
import com.demo.model.Event;
import com.demo.sender.EventSender;
import com.demo.sender.MockNetworkSender;

import java.util.concurrent.CountDownLatch;

public class ConcurrencyStressTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("====== 开始事件验证系统并发压测 ======");

        EventValidator validator = new EventValidator();
        EventSender sender = new MockNetworkSender(0.05);
        EventDispatcher dispatcher = new EventDispatcher(validator, sender);

        int totalEvents = 1000;
        CountDownLatch latch = new CountDownLatch(totalEvents);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalEvents; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    Event.Builder builder = new Event.Builder("UserAction_" + index)
                            .eventId("ID_" + index)
                            .addParam("platform", "Android");

                    if (index % 10 == 0) {
                        builder.addParam("value", -10);
                    } else if (index % 15 == 0) {
                        builder.addParam("currency", "rmb");
                    } else if (index % 20 == 0) {
                        builder = new Event.Builder("");
                    } else {
                        builder.addParam("value", 100).addParam("currency", "USD");
                    }

                    dispatcher.dispatch(builder.build());
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        dispatcher.shutdown();

        long costTime = System.currentTimeMillis() - startTime;

        System.out.println("\n====== 并发稳定性验证报告 ======");
        System.out.println("压测耗时: " + costTime + " ms");
        System.out.println("预期上报总数: " + totalEvents);
        System.out.println("系统接收总数: " + dispatcher.totalReceived.sum());
        System.out.println("校验失败拦截数: " + dispatcher.validateFailCount.sum());
        System.out.println("网络发送成功数: " + dispatcher.successCount.sum());
        System.out.println("网络发送失败数: " + dispatcher.sendFailCount.sum());

        long processedTotal = dispatcher.successCount.sum() + dispatcher.sendFailCount.sum() + dispatcher.validateFailCount.sum();
        boolean isLossless = (dispatcher.totalReceived.sum() == processedTotal);
        System.out.println("\n[一致性验证] 接收总数 == 处理总数 ? " + isLossless + " (证明没有丢事件)");

        boolean hasDuplicate = dispatcher.processTracker.values().stream().anyMatch(v -> v.get() > 1);
        System.out.println("[一致性验证] 是否存在被重复处理的事件 ? " + hasDuplicate + " (证明线程安全)");
        System.out.println("======================================");
    }
}