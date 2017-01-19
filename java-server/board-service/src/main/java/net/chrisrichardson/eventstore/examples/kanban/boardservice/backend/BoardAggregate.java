package net.chrisrichardson.eventstore.examples.kanban.boardservice.backend;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
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
