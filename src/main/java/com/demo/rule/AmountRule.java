package com.demo.rule;


import com.demo.model.Event;
import com.demo.exception.ValidationException;
public class AmountRule implements ValidationRule {
    @Override
    public void validate(Event event) throws ValidationException {
        Object val = event.getParams().get("value");
        if (val != null) {
            if (!(val instanceof Number)) {
                throw new ValidationException("金额 value 必须是数值类型");
            }
            if (((Number) val).longValue() <= 0) {
                throw new ValidationException("金额 value 必须大于 0");
            }
        }
    }
}