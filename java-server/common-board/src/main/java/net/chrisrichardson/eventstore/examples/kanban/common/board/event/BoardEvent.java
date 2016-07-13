package net.chrisrichardson.eventstore.examples.kanban.common.board.event;


import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "net.chrisrichardson.eventstore.examples.kanban.commandside.board.BoardAggregate")
public class BoardEvent implements Event {
}
