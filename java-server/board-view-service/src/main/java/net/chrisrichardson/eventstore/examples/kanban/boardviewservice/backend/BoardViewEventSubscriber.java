package net.chrisrichardson.eventstore.examples.kanban.boardviewservice.backend;

import io.eventuate.DispatchedEvent;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventSubscriber(id = "boardEventHandlers")
public class BoardViewEventSubscriber {

    private BoardUpdateService boardUpdateService;
    private static Logger log = LoggerFactory.getLogger(BoardViewEventSubscriber.class);

    public BoardViewEventSubscriber(BoardUpdateService boardInfoUpdateService) {
        this.boardUpdateService = boardInfoUpdateService;
    }

    @EventHandlerMethod
    public void create(DispatchedEvent<BoardCreatedEvent> de) {
        BoardCreatedEvent event = de.getEvent();
        String id = de.getEntityId();

        log.info("BoardViewEventSubscriber got event : {}", de.getEvent());
        boardUpdateService.create(id, event.getBoardInfo());
    }
}