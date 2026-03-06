package com.demo.core;

import com.demo.model.Event;
import com.demo.exception.ValidationException;
import com.demo.rule.*;

import java.util.ArrayList;
import java.util.List;

public class EventValidator {
    private final List<ValidationRule> rules = new ArrayList<>();

    public EventValidator() {
        rules.add(new RequiredFieldRule());
        rules.add(new AmountRule());
        rules.add(new CurrencyRule());
        rules.add(new AdRule());
    }

    public void addRule(ValidationRule rule) {
        rules.add(rule);
    }

    public void validateAll(Event event) throws ValidationException {
        for (ValidationRule rule : rules) {
            rule.validate(event);
        }
    }
}