package net.chrisrichardson.eventstore.examples.kanban.queryside.board;

import io.eventuate.DispatchedEvent;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventSubscriber(id = "boardEventHandlers")
public class BoardQueryWorkflow{

    private BoardUpdateService boardUpdateService;
    private static Logger log = LoggerFactory.getLogger(BoardQueryWorkflow.class);

    public BoardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
        this.boardUpdateService = boardInfoUpdateService;
    }

    @EventHandlerMethod
    public void create(DispatchedEvent<BoardCreatedEvent> de) {
        log.debug("BoardQueryWorkflow start processing event : {}", de.getEvent());
        BoardCreatedEvent event = de.getEvent();
        String id = de.getEntityId();

        boardUpdateService.create(id, event.getBoardInfo());
        log.debug("BoardQueryWorkflow finish processing event : {}", de.getEvent());
    }
}