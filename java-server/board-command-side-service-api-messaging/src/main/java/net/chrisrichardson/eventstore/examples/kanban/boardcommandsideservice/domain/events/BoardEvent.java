package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.events;


import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service.BoardAggregate")
public class BoardEvent implements Event {
}
