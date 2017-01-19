package net.chrisrichardson.eventstore.examples.kanban.queryside.task;

import io.eventuate.DispatchedEvent;
import io.eventuate.Event;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventSubscriber(id = "taskEventHandlers")
public class TaskQueryWorkflow {

  private TaskUpdateService taskUpdateService;

  public TaskQueryWorkflow(TaskUpdateService taskUpdateService) {
    this.taskUpdateService = taskUpdateService;
  }

  private static Logger log = LoggerFactory.getLogger(TaskQueryWorkflow.class);

  @EventHandlerMethod
  public void create(DispatchedEvent<TaskCreatedEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    String id = de.getEntityId();

    taskUpdateService.create(id, de.getEvent().getTaskInfo());
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void update(DispatchedEvent<TaskUpdatedEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setTaskDetails(de.getEvent().getTaskDetails());
    taskInfo.setUpdate(de.getEvent().getUpdate());

    taskUpdateService.update(de.getEntityId(), taskInfo);
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void complete(DispatchedEvent<TaskCompletedEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    processChangeStatusEvent(de, TaskStatus.completed);
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void delete(DispatchedEvent<TaskDeletedEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    taskUpdateService.delete(de.getEntityId());
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void schedule(DispatchedEvent<TaskScheduledEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    processChangeStatusEvent(de, TaskStatus.scheduled);
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void backlog(DispatchedEvent<TaskBacklogEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setUpdate(de.getEvent().getUpdate());
    taskInfo.setStatus(TaskStatus.backlog);

    updateAndSendEvent(de, taskInfo);
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  @EventHandlerMethod
  public void start(DispatchedEvent<TaskStartedEvent> de) {
    log.debug("TaskQueryWorkflow start processing event : {}", de.getEvent());
    processChangeStatusEvent(de, TaskStatus.started);
    log.debug("TaskQueryWorkflow finish processing event : {}", de.getEvent());
  }

  private void processChangeStatusEvent(DispatchedEvent<? extends DetailedTaskEvent> de, TaskStatus taskStatus) {
    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setBoardId(de.getEvent().getBoardId());
    taskInfo.setUpdate(de.getEvent().getUpdate());
    taskInfo.setStatus(taskStatus);

    updateAndSendEvent((DispatchedEvent<? extends Event>) de, taskInfo);
  }

  private void updateAndSendEvent(DispatchedEvent<? extends Event> de, TaskInfo taskInfo) {
    taskUpdateService.update(de.getEntityId(), taskInfo);
  }
}