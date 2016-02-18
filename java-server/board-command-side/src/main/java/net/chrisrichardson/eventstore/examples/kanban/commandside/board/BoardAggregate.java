package net.chrisrichardson.eventstore.examples.kanban.commandside.board;

import net.chrisrichardson.eventstore.Event;
import net.chrisrichardson.eventstore.EventUtil;
import net.chrisrichardson.eventstore.ReflectiveMutableCommandProcessingAggregate;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by popikyardo on 21.09.15.
 */
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
