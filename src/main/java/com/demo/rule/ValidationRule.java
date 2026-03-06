package com.demo.rule;

import com.demo.model.Event;
import com.demo.exception.ValidationException;

public interface ValidationRule {
    void validate(Event event) throws ValidationException;
}