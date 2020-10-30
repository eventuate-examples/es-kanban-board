package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.DispatchedEvent;
import io.eventuate.Event;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.events.BoardCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.Task;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.KanbanWebSocketEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskBacklogEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskCompletedEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskCreatedEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskDeletedEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskScheduledEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskStartedEvent;
import net.chrisrichardson.eventstore.examples.kanban.domain.events.TaskUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@EventSubscriber(id = "websocketEventHandlers")
public class WebsocketEventsTranslator {

  protected SimpMessagingTemplate template;

  private static String DESTINATION_DEFAULT_URL = "/events";


  private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

  private static Logger log = LoggerFactory.getLogger(WebsocketEventsTranslator.class);

  public WebsocketEventsTranslator(SimpMessagingTemplate template) {
    this.template = template;
  }

  @EventHandlerMethod
  public void sendBoardEvents(DispatchedEvent<BoardCreatedEvent> de) throws JsonProcessingException {
    Board board = new Board();
    board.setId(de.getEntityId());
    board.setTitle(de.getEvent().getBoardInfo().getTitle());
    board.setCreatedBy(de.getEvent().getBoardInfo().getCreation().getWho());
    board.setCreatedDate(de.getEvent().getBoardInfo().getCreation().getWhen());
    board.setUpdatedBy(de.getEvent().getBoardInfo().getUpdate().getWho());
    board.setUpdatedDate(de.getEvent().getBoardInfo().getUpdate().getWhen());

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(board));
  }

  @EventHandlerMethod
  public void sendTaskCreatedEvent(DispatchedEvent<TaskCreatedEvent> de) throws JsonProcessingException {
    Task task = Task.transform(de.getEntityId(), de.getEvent().getTaskInfo());
    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskMovedToBacklogEvent(DispatchedEvent<TaskBacklogEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());
    task.setBoardId("");
    task.setStatus(TaskStatus.backlog);

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskCompletedEvent(DispatchedEvent<TaskCompletedEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setBoardId(de.getEvent().getBoardId());
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());
    task.setStatus(TaskStatus.completed);

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskDeletedEvent(DispatchedEvent<TaskDeletedEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());
    task.setDeleted(true);

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskScheduledEvent(DispatchedEvent<TaskScheduledEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setBoardId(de.getEvent().getBoardId());
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());
    task.setStatus(TaskStatus.scheduled);

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskStartedEvent(DispatchedEvent<TaskStartedEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setBoardId(de.getEvent().getBoardId());
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());
    task.setStatus(TaskStatus.started);

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }

  @EventHandlerMethod
  public void sendTaskUpdatedEvent(DispatchedEvent<TaskUpdatedEvent> de) throws JsonProcessingException {
    Task task = new Task();
    task.setId(de.getEntityId());
    task.setTitle(de.getEvent().getTaskDetails().getTitle());
    task.setDescription(de.getEvent().getTaskDetails().getDescription() != null ?
            de.getEvent().getTaskDetails().getDescription().getDescription() : null);
    task.setUpdatedBy(de.getEvent().getUpdate().getWho());
    task.setUpdatedDate(de.getEvent().getUpdate().getWhen());

    this.sendEvent(de, DESTINATION_DEFAULT_URL, objectMapper.writeValueAsString(task));
  }


  private void sendEvent(DispatchedEvent<? extends Event> de, String destination, String eventData) throws JsonProcessingException {
    log.info("Sending board event to websocket : {}", de.getEvent());
    KanbanWebSocketEvent event = new KanbanWebSocketEvent();
    event.setEntityId(de.getEntityId());
    event.setEventData(eventData);
    event.setEventId(de.getEventId().asString());
    event.setEventType(de.getEventType().getName());
    template.convertAndSend(destination, objectMapper.writeValueAsString(event));
  }
}