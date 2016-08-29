package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import io.eventuate.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.ChangeTaskStatusRequest;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.HistoryEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.HistoryResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api")
public class TaskCommandController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskHistoryService taskHistoryService;

    private static Logger log = LoggerFactory.getLogger(TaskCommandController.class);

    @RequestMapping(value = "/tasks", method = POST)
    public CompletableFuture<TaskResponse> saveTask(@RequestBody TaskInfo task) {
        return taskService.save(task).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}", method = DELETE)
    public CompletableFuture<TaskResponse> deleteTask(@PathVariable("id") String taskId) {
        return taskService.remove(taskId, getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}", method = PUT)
    public CompletableFuture<TaskResponse> updateTask(@PathVariable("id") String taskId,
                                                      @RequestBody TaskDetails request) {
        return taskService.update(taskId, request, getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/start", method = PUT)
    public CompletableFuture<TaskResponse> startTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.startTask(taskId, request.getBoardId(), getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/schedule", method = PUT)
    public CompletableFuture<TaskResponse> scheduleTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.scheduleTask(taskId, request.getBoardId(), getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/complete", method = PUT)
    public CompletableFuture<TaskResponse> completeTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.completeTask(taskId, request.getBoardId(), getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/backlog", method = PUT)
    public CompletableFuture<TaskResponse> backlogTask(@PathVariable("id") String taskId) {
        return taskService.backlogTask(taskId, getCurrentUser()).thenApply(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/history", method = GET)
    public CompletableFuture<HistoryResponse> getHistory(@PathVariable("id") String taskId) {
        return taskHistoryService.getHistoryEvents(taskId).thenApply(ewm -> {
            log.info("Getting Task History {}", ewm.getEntity().getTask());
            return new HistoryResponse(ewm.getEvents().stream().map(e -> {
                HistoryEvent res = new HistoryEvent();
                res.setId(ewm.getEntityIdAndVersion().getEntityId());
                res.setEventType(e.getClass().getCanonicalName());
                res.setEventData(e);
                return res;
            }).collect(Collectors.toList()));
        });
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private TaskResponse makeTaskResponse(EntityWithIdAndVersion<TaskAggregate> e) {
        return new TaskResponse(e.getEntityId(),
                e.getAggregate().getTask().getBoardId(),
                e.getAggregate().getTask().getTaskDetails().getTitle(),
                e.getAggregate().getTask().getCreation().getWho(),
                e.getAggregate().getTask().getUpdate().getWho(),
                e.getAggregate().getTask().getCreation().getWhen(),
                e.getAggregate().getTask().getUpdate().getWhen(),
                e.getAggregate().getTask().getStatus(),
                e.getAggregate().getTask().isDeleted(),
                e.getAggregate().getTask().getTaskDetails().getDescription()
        );
    }
}
