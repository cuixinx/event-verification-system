package com.demo.rule;

import com.demo.model.Event;
import com.demo.exception.ValidationException;

public class RequiredFieldRule implements ValidationRule {
    @Override
    public void validate(Event event) throws ValidationException {
        if (event.getEventName() == null || event.getEventName().trim().isEmpty()) {
            throw new ValidationException("eventName 不能为空");
        }
        if (event.getTimestamp() <= 0) {
            throw new ValidationException("timestamp 无效");
        }
        if (event.getParams() == null || event.getParams().isEmpty()) {
            throw new ValidationException("params 不能为空");
        }
    }
}