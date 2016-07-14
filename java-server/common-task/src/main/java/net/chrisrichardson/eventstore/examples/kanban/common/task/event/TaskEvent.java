package net.chrisrichardson.eventstore.examples.kanban.common.task.event;


import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity="net.chrisrichardson.eventstore.examples.kanban.commandside.task.TaskAggregate")
public abstract class TaskEvent implements Event {
}
