package net.chrisrichardson.eventstore.examples.kanban.queryside.task;

import net.chrisrichardson.eventstore.Event;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.*;
import net.chrisrichardson.eventstore.subscriptions.CompoundEventHandler;
import net.chrisrichardson.eventstore.subscriptions.DispatchedEvent;
import net.chrisrichardson.eventstore.subscriptions.EventHandlerMethod;
import net.chrisrichardson.eventstore.subscriptions.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Created by popikyardo on 15.10.15.
 */
@EventSubscriber(id = "taskEventHandlers")
public class TaskQueryWorkflow implements CompoundEventHandler {

    private TaskUpdateService taskUpdateService;

    public TaskQueryWorkflow(TaskUpdateService taskUpdateService) {
        this.taskUpdateService = taskUpdateService;
    }

    private static Logger log = LoggerFactory.getLogger(TaskQueryWorkflow.class);

    @EventHandlerMethod
    public Observable<Object> create(DispatchedEvent<TaskCreatedEvent> de) {
        String id = de.getEntityIdentifier().getId();

        taskUpdateService.create(id, de.event().getTaskInfo());

        return Observable.just(null);
    }

    @EventHandlerMethod
    public Observable<Object> update(DispatchedEvent<TaskUpdatedEvent> de) {
        log.info("TaskQueryWorkflow got event : {}", de.event());
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskDetails(de.event().getTaskDetails());
        taskInfo.setUpdate(de.event().getUpdate());

        taskUpdateService.update(de.entityId().id(), taskInfo);

        return Observable.just(null);
    }

    @EventHandlerMethod
    public Observable<Object> complete(DispatchedEvent<TaskCompletedEvent> de) {
        return processChangeStatusEvent(de, TaskStatus.completed);
    }

    @EventHandlerMethod
    public Observable<Object> delete(DispatchedEvent<TaskDeletedEvent> de) {
        log.info("TaskQueryWorkflow got event : {}", de.event());
        taskUpdateService.delete(de.getEntityIdentifier().getId());

        return Observable.just(null);
    }

    @EventHandlerMethod
    public Observable<Object> schedule(DispatchedEvent<TaskScheduledEvent> de) {
        return processChangeStatusEvent(de, TaskStatus.scheduled);
    }

    @EventHandlerMethod
    public Observable<Object> backlog(DispatchedEvent<TaskBacklogEvent> de) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setUpdate(de.event().getUpdate());
        taskInfo.setStatus(TaskStatus.backlog);

        return updateAndSendEvent(de, taskInfo);
    }

    @EventHandlerMethod
    public Observable<Object> start(DispatchedEvent<TaskStartedEvent> de) {
        return processChangeStatusEvent(de, TaskStatus.started);
    }

    private Observable<Object> processChangeStatusEvent(DispatchedEvent<? extends DetailedTaskEvent> de, TaskStatus taskStatus) {

        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setBoardId(de.event().getBoardId());
        taskInfo.setUpdate(de.event().getUpdate());
        taskInfo.setStatus(taskStatus);

        return updateAndSendEvent((DispatchedEvent<? extends Event>) de, taskInfo);
    }

    private Observable<Object> updateAndSendEvent(DispatchedEvent<? extends Event> de, TaskInfo taskInfo) {
        log.info("TaskQueryWorkflow got event : {}", de.event());
        taskUpdateService.update(de.entityId().id(), taskInfo);

        return Observable.just(null);
    }
}