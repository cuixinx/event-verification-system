package com.demo.rule;


import com.demo.model.Event;
import com.demo.exception.ValidationException;

public class CurrencyRule implements ValidationRule {
    @Override
    public void validate(Event event) throws ValidationException {
        Object currency = event.getParams().get("currency");
        if (currency != null && !String.valueOf(currency).matches("^[A-Z]{3}$")) {
            throw new ValidationException("currency 格式错误，必须为3位大写字母");
        }
    }
}