package net.chrisrichardson.eventstore.examples.kanban.taskviewservice.backend;

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
public class TaskViewEventSubscriber {

    private TaskUpdateService taskUpdateService;

    public TaskViewEventSubscriber(TaskUpdateService taskUpdateService) {
        this.taskUpdateService = taskUpdateService;
    }

    private static Logger log = LoggerFactory.getLogger(TaskViewEventSubscriber.class);

    @EventHandlerMethod
    public void create(DispatchedEvent<TaskCreatedEvent> de) {
        String id = de.getEntityId();

        taskUpdateService.create(id, de.getEvent().getTaskInfo());
    }

    @EventHandlerMethod
    public void update(DispatchedEvent<TaskUpdatedEvent> de) {
        log.info("TaskViewEventSubscriber got event : {}", de.getEvent());
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskDetails(de.getEvent().getTaskDetails());
        taskInfo.setUpdate(de.getEvent().getUpdate());

        taskUpdateService.update(de.getEntityId(), taskInfo);
    }

    @EventHandlerMethod
    public void complete(DispatchedEvent<TaskCompletedEvent> de) {
        processChangeStatusEvent(de, TaskStatus.completed);
    }

    @EventHandlerMethod
    public void delete(DispatchedEvent<TaskDeletedEvent> de) {
        log.info("TaskViewEventSubscriber got event : {}", de.getEvent());
        taskUpdateService.delete(de.getEntityId());
    }

    @EventHandlerMethod
    public void schedule(DispatchedEvent<TaskScheduledEvent> de) {
        processChangeStatusEvent(de, TaskStatus.scheduled);
    }

    @EventHandlerMethod
    public void backlog(DispatchedEvent<TaskBacklogEvent> de) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setUpdate(de.getEvent().getUpdate());
        taskInfo.setStatus(TaskStatus.backlog);

        updateAndSendEvent(de, taskInfo);
    }

    @EventHandlerMethod
    public void start(DispatchedEvent<TaskStartedEvent> de) {
        processChangeStatusEvent(de, TaskStatus.started);
    }

    private void processChangeStatusEvent(DispatchedEvent<? extends DetailedTaskEvent> de, TaskStatus taskStatus) {

        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setBoardId(de.getEvent().getBoardId());
        taskInfo.setUpdate(de.getEvent().getUpdate());
        taskInfo.setStatus(taskStatus);

        updateAndSendEvent((DispatchedEvent<? extends Event>) de, taskInfo);
    }

    private void updateAndSendEvent(DispatchedEvent<? extends Event> de, TaskInfo taskInfo) {
        log.info("TaskViewEventSubscriber got event : {}", de.getEvent());
        taskUpdateService.update(de.getEntityId(), taskInfo);
    }
}