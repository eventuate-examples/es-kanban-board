package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.Event;
import net.chrisrichardson.eventstore.examples.kanban.common.board.event.BoardCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.Board;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.*;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.Task;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import net.chrisrichardson.eventstore.subscriptions.CompoundEventHandler;
import net.chrisrichardson.eventstore.subscriptions.DispatchedEvent;
import net.chrisrichardson.eventstore.subscriptions.EventHandlerMethod;
import net.chrisrichardson.eventstore.subscriptions.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rx.Observable;

/**
 * Created by popikyardo on 15.10.15.
 */
@EventSubscriber(id="websocketEventHandlers")
public class WebsocketEventsTranslator implements CompoundEventHandler {

    protected SimpMessagingTemplate template;

    private static String DESTINATION_DEFAULT_URL = "/events";


    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static Logger log = LoggerFactory.getLogger(WebsocketEventsTranslator.class);

    public WebsocketEventsTranslator(SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventHandlerMethod
    public Observable<Object> sendBoardEvents(DispatchedEvent<BoardCreatedEvent> de) throws JsonProcessingException {
        Board board = new Board();
        board.setId(de.getEntityIdentifier().getId());
        board.setTitle(de.event().getBoardInfo().getTitle());
        board.setCreatedBy(de.event().getBoardInfo().getCreation().getWho());
        board.setCreatedDate(de.event().getBoardInfo().getCreation().getWhen());
        board.setUpdatedBy(de.event().getBoardInfo().getUpdate().getWho());
        board.setUpdatedDate(de.event().getBoardInfo().getUpdate().getWhen());

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(board));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskCreatedEvent(DispatchedEvent<TaskCreatedEvent> de) throws JsonProcessingException {
        Task task = Task.transform(de.getEntityIdentifier().getId(), de.event().getTaskInfo());
        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskMovedToBacklogEvent(DispatchedEvent<TaskBacklogEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());
        task.setBoardId("");
        task.setStatus(TaskStatus.backlog);

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskCompletedEvent(DispatchedEvent<TaskCompletedEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setBoardId(de.event().getBoardId());
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());
        task.setStatus(TaskStatus.completed);

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskDeletedEvent(DispatchedEvent<TaskDeletedEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());
        task.setDeleted(true);

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskScheduledEvent(DispatchedEvent<TaskScheduledEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setBoardId(de.event().getBoardId());
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());
        task.setStatus(TaskStatus.scheduled);

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskStartedEvent(DispatchedEvent<TaskStartedEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setBoardId(de.event().getBoardId());
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());
        task.setStatus(TaskStatus.started);

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }

    @EventHandlerMethod
    public Observable<Object> sendTaskUpdatedEvent(DispatchedEvent<TaskUpdatedEvent> de) throws JsonProcessingException {
        Task task = new Task();
        task.setId(de.getEntityIdentifier().getId());
        task.setTitle(de.event().getTaskDetails().getTitle());
        task.setDescription(de.event().getTaskDetails().getDescription() != null ?
                de.event().getTaskDetails().getDescription().getDescription() : null);
        task.setUpdatedBy(de.event().getUpdate().getWho());
        task.setUpdatedDate(de.event().getUpdate().getWhen());

        return this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
    }


    private Observable<Object> sendEvent(DispatchedEvent<? extends Event> de, String destination, String eventData) throws JsonProcessingException {
        log.info("Sending board event to websocket : {}", de.event());
        KanbanWebSocketEvent event = new KanbanWebSocketEvent();
        event.setEntityId(de.entityId().id());
        event.setEventData(eventData);
        event.setEventId(de.eventId().asString());
        event.setEventType(de.eventType());
        template.convertAndSend(destination, objectMapper.writeValueAsString(event));

        return Observable.just(null);
    }
}