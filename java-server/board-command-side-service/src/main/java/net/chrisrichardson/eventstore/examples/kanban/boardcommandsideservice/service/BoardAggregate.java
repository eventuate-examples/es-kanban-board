package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands.BoardCommand;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands.CreateBoardCommand;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.events.BoardCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoardAggregate extends ReflectiveMutableCommandProcessingAggregate<BoardAggregate, BoardCommand> {

  private BoardInfo board;

  private static Logger log = LoggerFactory.getLogger(BoardAggregate.class);

  public List<Event> process(CreateBoardCommand cmd) {
    log.info("Calling BoardAggregate.process for CreateBoardCommand : {}", cmd);
    return EventUtil.events(new BoardCreatedEvent(cmd.getBoardInfo()));
  }

  public void apply(BoardCreatedEvent event) {
    log.info("Calling BoardAggregate.APPLY for BoardCreatedEvent : {}", event);

    this.board = event.getBoardInfo();
    this.board.setCreation(event.getBoardInfo().getCreation());
    this.board.setUpdate(event.getBoardInfo().getUpdate());
    this.board.setTitle(event.getBoardInfo().getTitle());
  }

  public BoardInfo getBoard() {
    return board;
  }
}
