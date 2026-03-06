package com.demo.sender;

import com.demo.model.Event;
public interface EventSender {
    boolean send(Event event);
}