package net.chrisrichardson.eventstore.examples.kanban.domain.events;


import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.service.TaskAggregate")
public abstract class TaskEvent implements Event {
}
