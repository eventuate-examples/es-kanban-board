package net.chrisrichardson.eventstore.examples.kanban.common.task.model;


import io.eventuate.Event;

public class HistoryEvent {
    private String id;
    private String eventType;
    private Event eventData;

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

    public Event getEventData() {
        return eventData;
    }

    public void setEventData(Event eventData) {
        this.eventData = eventData;
    }
}
