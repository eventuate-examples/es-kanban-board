package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model;


import org.apache.commons.lang.builder.ToStringBuilder;

public class KanbanWebSocketEvent {

  private String eventId;
  private String eventType;
  private String eventData;
  private String entityId;

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getEventData() {
    return eventData;
  }

  public void setEventData(String eventData) {
    this.eventData = eventData;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
