package com.demo.rule;

import com.demo.model.Event;
import com.demo.exception.ValidationException;

public class AdRule implements ValidationRule {
    @Override
    public void validate(Event event) throws ValidationException {
        Object adUnitId = event.getParams().get("adUnitId");
        if (adUnitId != null && String.valueOf(adUnitId).trim().isEmpty()) {
            throw new ValidationException("adUnitId 存在时不能为空");
        }
        Object placement = event.getParams().get("placement");
        if (placement != null && String.valueOf(placement).trim().isEmpty()) {
            throw new ValidationException("placement 存在时不能为空");
        }
    }
}