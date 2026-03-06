package com.demo.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private final String eventId;
    private final String eventName;
    private final long timestamp;
    private final Map<String, Object> params;

    private Event(Builder builder) {
        this.eventId = builder.eventId != null ? builder.eventId : UUID.randomUUID().toString();
        this.eventName = builder.eventName;
        this.timestamp = builder.timestamp > 0 ? builder.timestamp : System.currentTimeMillis();
        this.params = builder.params.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(builder.params));
    }

    public String getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public long getTimestamp() { return timestamp; }
    public Map<String, Object> getParams() { return params; }

    public static class Builder {
        private String eventId;
        private String eventName;
        private long timestamp;
        private final Map<String, Object> params = new HashMap<>();

        public Builder(String eventName) { this.eventName = eventName; }
        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder timestamp(long timestamp) { this.timestamp = timestamp; return this; }
        public Builder addParam(String key, Object value) {
            if (key != null && value != null) this.params.put(key, value);
            return this;
        }
        public Event build() { return new Event(this); }
    }
}