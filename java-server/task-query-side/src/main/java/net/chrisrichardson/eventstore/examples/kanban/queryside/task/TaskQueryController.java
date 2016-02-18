package net.chrisrichardson.eventstore.examples.kanban.queryside.task;

import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.BacklogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by popikyardo on 19/09/2015.
 */
@RestController
public class TaskQueryController {

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "api/tasks", method = GET)
    public ResponseEntity<BacklogResponse> listAllTasks(@RequestParam(value = "boardId", required = false) String boardId) {
        BacklogResponse resp = new BacklogResponse();
        resp.setBacklog(Optional.of(taskRepository.
                findByStatus(TaskStatus.backlog))
                .orElse(new ArrayList<>()));
        if (boardId != null && !boardId.isEmpty()) {
            resp.setTasks(taskRepository.findByBoardIdAndStatusNot(boardId, TaskStatus.backlog));
        } else {
            resp.setTasks(new ArrayList<>());
        }
        return new ResponseEntity<>(resp, OK);
    }
}
