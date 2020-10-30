package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.service;

import io.eventuate.DispatchedEvent;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.events.BoardCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventSubscriber(id = "boardEventHandlers")
public class BoardQueryWorkflow {

  private BoardUpdateService boardUpdateService;
  private static Logger log = LoggerFactory.getLogger(BoardQueryWorkflow.class);

  public BoardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
    this.boardUpdateService = boardInfoUpdateService;
  }

  @EventHandlerMethod
  public void create(DispatchedEvent<BoardCreatedEvent> de) {
    BoardCreatedEvent event = de.getEvent();
    String id = de.getEntityId();

    log.info("BoardQueryWorkflow got event : {}", de.getEvent());
    boardUpdateService.create(id, event.getBoardInfo());
  }
}