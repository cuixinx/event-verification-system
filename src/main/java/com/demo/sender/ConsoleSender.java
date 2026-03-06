package com.demo.sender;

import com.demo.model.Event;

public class ConsoleSender implements EventSender {
    @Override
    public boolean send(Event event) {
        System.out.println("[ConsoleSender] 成功发送事件: " + event.getEventName() + " | ID: " + event.getEventId());
        return true;
    }
}