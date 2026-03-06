package com.demo.sender;

import com.demo.model.Event;
import java.util.Random;

public class MockNetworkSender implements EventSender {
    private final double failureRate;
    private final Random random = new Random();

    public MockNetworkSender(double failureRate) {
        this.failureRate = failureRate;
    }

    @Override
    public boolean send(Event event) {
        try {
            // 模拟网络延迟 10-50ms
            Thread.sleep(10 + random.nextInt(40));
            // 模拟丢包/失败
            if (random.nextDouble() < failureRate) {
                System.err.println("[MockSender] 网络请求失败: " + event.getEventName());
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}