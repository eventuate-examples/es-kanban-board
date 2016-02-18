package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.Event;
import net.chrisrichardson.eventstore.EventUtil;
import net.chrisrichardson.eventstore.ReflectiveMutableCommandProcessingAggregate;
import net.chrisrichardson.eventstore.examples.kanban.common.task.*;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by popikyardo on 21.09.15.
 */
public class TaskAggregate extends ReflectiveMutableCommandProcessingAggregate<TaskAggregate, TaskCommand> {

    private TaskInfo task;

    private static Logger log = LoggerFactory.getLogger(TaskAggregate.class);

    public List<Event> process(CreateTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for CreateTaskCommand : {}", cmd);
        return EventUtil.events(new TaskCreatedEvent(cmd.getTaskInfo()));
    }

    public List<Event> process(UpdateTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for UpdateTaskCommand : {}", cmd);
        return EventUtil.events(new TaskUpdatedEvent(cmd.getTaskDetails(),
                cmd.getUpdate()));
    }

    public List<Event> process(DeleteTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for UpdateTaskCommand : {}", cmd);
        return EventUtil.events(new TaskDeletedEvent(cmd.getUpdate()));
    }

    public List<Event> process(StartTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for StartTaskCommand : {}", cmd);
        return EventUtil.events(new TaskStartedEvent(cmd.getBoardId(),
                cmd.getUpdate()));
    }

    public List<Event> process(ScheduleTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for ScheduleTaskCommand : {}", cmd);
        return EventUtil.events(new TaskScheduledEvent(cmd.getBoardId(),
                cmd.getUpdate()));
    }

    public List<Event> process(CompleteTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for CompleteTaskCommand : {}", cmd);
        return EventUtil.events(new TaskCompletedEvent(cmd.getBoardId(),
                cmd.getUpdate()));
    }

    public List<Event> process(MoveToBacklogTaskCommand cmd) {
        log.info("Calling TaskAggregate.process for BacklogTaskCommand : {}", cmd);
        return EventUtil.events(new TaskBacklogEvent(cmd.getUpdate()));
    }

    public void apply(TaskCreatedEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskCreatedEvent : {}", event);

        this.task = event.getTaskInfo();
        this.task.setUpdate(event.getTaskInfo().getUpdate());
        this.task.setCreation(event.getTaskInfo().getCreation());
        this.task.setStatus(TaskStatus.backlog);
    }

    public void apply(TaskDeletedEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskDeletedEvent : {}", event);
        this.task.setUpdate(event.getUpdate());
        this.task.setDeleted(true);
    }

    public void apply(TaskUpdatedEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskUpdatedEvent : {}", event);
        this.task.setTaskDetails(event.getTaskDetails());
        this.task.setUpdate(event.getUpdate());
    }

    public void apply(TaskStartedEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskStartedEvent : {}", event);
        this.task.setStatus(TaskStatus.started);
        this.task.setBoardId(event.getBoardId());
        this.task.setUpdate(event.getUpdate());
    }

    public void apply(TaskScheduledEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskScheduledEvent : {}", event);
        this.task.setStatus(TaskStatus.scheduled);
        this.task.setBoardId(event.getBoardId());
        this.task.setUpdate(event.getUpdate());
    }

    public void apply(TaskCompletedEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskCompletedEvent : {}", event);
        this.task.setStatus(TaskStatus.completed);
        this.task.setBoardId(event.getBoardId());
        this.task.setUpdate(event.getUpdate());
    }

    public void apply(TaskBacklogEvent event) {
        log.info("Calling TaskAggregate.APPLY for TaskMovedToBacklogEvent : {}", event);
        this.task.setStatus(TaskStatus.backlog);
        this.task.setUpdate(event.getUpdate());
    }

    public TaskInfo getTask() {
        return task;
    }
}