package com.demo;

import com.demo.core.EventValidator;
import com.demo.exception.ValidationException;
import com.demo.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private EventValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new EventValidator();
    }

    @Test
    public void testValidEvent_ShouldPass() {
        Event event = new Event.Builder("AdClick")
                .addParam("value", 150)
                .addParam("currency", "USD")
                .addParam("adUnitId", "banner_01")
                .build();

        assertDoesNotThrow(() -> validator.validateAll(event), "合规事件不应抛出异常");
    }

    @Test
    public void testMissingRequiredField_ShouldFail() {
        Event event1 = new Event.Builder("")
                .addParam("key", "val")
                .build();

        Exception ex1 = assertThrows(ValidationException.class, () -> validator.validateAll(event1));
        assertTrue(ex1.getMessage().contains("eventName"));
    }

    @Test
    public void testInvalidAmount_ShouldFail() {
        Event eventNegative = new Event.Builder("Purchase")
                .addParam("value", -100)
                .build();
        assertThrows(ValidationException.class, () -> validator.validateAll(eventNegative));
    }
}