package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.EntityWithIdAndVersion;
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
import rx.Observable;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Main on 02.10.2015.
 */
@RestController
@RequestMapping(value = "/api")
public class TaskCommandController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskHistoryService taskHistoryService;

    private static Logger log = LoggerFactory.getLogger(TaskCommandController.class);

    @RequestMapping(value = "/tasks", method = POST)
    public Observable<TaskResponse> saveTask(@RequestBody TaskInfo task) {
        return taskService.save(task).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}", method = DELETE)
    public Observable<TaskResponse> deleteTask(@PathVariable("id") String taskId) {
        return taskService.remove(taskId, getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}", method = PUT)
    public Observable<TaskResponse> updateTask(@PathVariable("id") String taskId,
                                               @RequestBody TaskDetails request) {
        return taskService.update(taskId, request, getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/start", method = PUT)
    public Observable<TaskResponse> startTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.startTask(taskId, request.getBoardId(), getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/schedule", method = PUT)
    public Observable<TaskResponse> scheduleTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.scheduleTask(taskId, request.getBoardId(), getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/complete", method = PUT)
    public Observable<TaskResponse> completeTask(@PathVariable("id") String taskId, @RequestBody ChangeTaskStatusRequest request) {
        return taskService.completeTask(taskId, request.getBoardId(), getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/backlog", method = PUT)
    public Observable<TaskResponse> backlogTask(@PathVariable("id") String taskId) {
        return taskService.backlogTask(taskId, getCurrentUser()).map(this::makeTaskResponse);
    }

    @RequestMapping(value = "/tasks/{id}/history", method = GET)
    public Observable<HistoryResponse> getHistory(@PathVariable("id") String taskId) {
        return taskHistoryService.getHistoryEvents(taskId).map(ewm -> {
            log.info("Getting Task History {}", ewm.entity().getTask());
            return new HistoryResponse(ewm.getEvents().stream().map(e -> {
                HistoryEvent res = new HistoryEvent();
                res.setId(ewm.entityId().id());
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
        return new TaskResponse(e.getEntityIdentifier().getId(),
                e.entity().getTask().getBoardId(),
                e.entity().getTask().getTaskDetails().getTitle(),
                e.entity().getTask().getCreation().getWho(),
                e.entity().getTask().getUpdate().getWho(),
                e.entity().getTask().getCreation().getWhen(),
                e.entity().getTask().getUpdate().getWhen(),
                e.entity().getTask().getStatus(),
                e.entity().getTask().isDeleted(),
                e.entity().getTask().getTaskDetails().getDescription()
        );
    }
}
