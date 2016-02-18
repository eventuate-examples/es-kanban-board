package net.chrisrichardson.eventstore.examples.kanban.queryside.board;

import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import net.chrisrichardson.eventstore.subscriptions.CompoundEventHandler;
import net.chrisrichardson.eventstore.subscriptions.DispatchedEvent;
import net.chrisrichardson.eventstore.subscriptions.EventHandlerMethod;
import net.chrisrichardson.eventstore.subscriptions.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Created by popikyardo on 21.09.15.
 */
@EventSubscriber(id = "boardEventHandlers")
public class BoardQueryWorkflow implements CompoundEventHandler {

    private BoardUpdateService boardUpdateService;
    private static Logger log = LoggerFactory.getLogger(BoardQueryWorkflow.class);

    public BoardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
        this.boardUpdateService = boardInfoUpdateService;
    }

    @EventHandlerMethod
    public Observable<Object> create(DispatchedEvent<BoardCreatedEvent> de) {
        BoardCreatedEvent event = de.event();
        String id = de.getEntityIdentifier().getId();

        log.info("BoardQueryWorkflow got event : {}", de.event());
        boardUpdateService.create(id, event.getBoardInfo());

        return Observable.just(null);
    }
}