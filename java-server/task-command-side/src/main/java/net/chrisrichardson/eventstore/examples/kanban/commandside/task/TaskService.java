package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import io.eventuate.AggregateRepository;
import io.eventuate.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class TaskService {

    private final AggregateRepository<TaskAggregate, TaskCommand> aggregateRepository;

    private static Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(AggregateRepository<TaskAggregate, TaskCommand> aggregateRepository) {
        this.aggregateRepository = aggregateRepository;
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> save(TaskInfo task) {
        log.info("TaskService saving : {}", task);
        return aggregateRepository.save(new CreateTaskCommand(task));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> update(String id, TaskDetails request, String updatedBy) {
        log.info("TaskService updating {}: {}", id, request);
        return aggregateRepository.update(id, new UpdateTaskCommand(request,
                new AuditEntry(updatedBy, new Date())));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> remove(String id, String updatedBy) {
        log.info("TaskService deleting : {}", id);
        return aggregateRepository.update(id, new DeleteTaskCommand(new AuditEntry(updatedBy, new Date())));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> startTask(String id, String boardId, String updatedBy) {
        log.info("TaskService starting task : {}", id);
        return aggregateRepository.update(id, new StartTaskCommand(boardId,
                new AuditEntry(updatedBy, new Date())));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> scheduleTask(String id, String boardId, String updatedBy) {
        log.info("TaskService scheduling task : {}", id);
        return aggregateRepository.update(id, new ScheduleTaskCommand(boardId,
                new AuditEntry(updatedBy, new Date())));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> completeTask(String id, String boardId, String updatedBy) {
        log.info("TaskService completing task : {}", id);
        return aggregateRepository.update(id, new CompleteTaskCommand(boardId,
                new AuditEntry(updatedBy, new Date())));
    }

    public CompletableFuture<EntityWithIdAndVersion<TaskAggregate>> backlogTask(String id, String updatedBy) {
        log.info("TaskService moving task to backlog : {}", id);
        return aggregateRepository.update(id, new MoveToBacklogTaskCommand(new AuditEntry(updatedBy, new Date())));
    }
}
