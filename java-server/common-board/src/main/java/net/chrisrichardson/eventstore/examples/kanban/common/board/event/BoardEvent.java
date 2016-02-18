package net.chrisrichardson.eventstore.examples.kanban.common.board.event;

import net.chrisrichardson.eventstore.Event;
import net.chrisrichardson.eventstore.EventEntity;

/**
 * Created by popikyardo on 20.10.15.
 */
@EventEntity(entity = "net.chrisrichardson.eventstore.examples.kanban.commandside.board.BoardAggregate")
public class BoardEvent implements Event {
}
