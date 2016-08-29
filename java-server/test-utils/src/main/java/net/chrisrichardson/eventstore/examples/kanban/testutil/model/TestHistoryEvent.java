package net.chrisrichardson.eventstore.examples.kanban.testutil.model;

import java.util.Map;


public class TestHistoryEvent {
    private String id;
    private String eventType;
    private Map<String, Object> eventData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }
}
