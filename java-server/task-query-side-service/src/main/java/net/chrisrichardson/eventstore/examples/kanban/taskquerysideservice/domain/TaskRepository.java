package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.domain;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskStatus;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by popikyardo on 15.10.15.
 */
public interface TaskRepository extends MongoRepository<Task, String> {

  List<Task> findByBoardIdAndStatusNot(String boardId, TaskStatus taskStatus);

  List<Task> findByStatus(TaskStatus taskStatus);
}
