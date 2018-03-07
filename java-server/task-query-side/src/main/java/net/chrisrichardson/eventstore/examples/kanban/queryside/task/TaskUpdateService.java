package net.chrisrichardson.eventstore.examples.kanban.queryside.task;

import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.model.Task;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by popikyardo on 15.10.15.
 */
public class TaskUpdateService {
    private TaskRepository taskRepository;

    public TaskUpdateService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(String id, TaskInfo taskInfo) {
        Task taskToCreate = Task.transform(id, taskInfo);
        return taskRepository.save(taskToCreate);
    }

    public Task delete(String id) {
        Optional<Task> taskToDelete = taskRepository.findById(id);
        taskToDelete.ifPresent(taskRepository::delete);
        return taskToDelete.orElse(null);
    }

    public Task update(String id, TaskInfo taskInfo) {
        Optional<Task> taskToUpdateOptional = taskRepository.findById(id);

        if (!taskToUpdateOptional.isPresent()) {
            throw new NoSuchElementException(String.format("Task with id %s doesn't exist", id));
        }

        Task taskToUpdate = taskToUpdateOptional.get();

        if(taskInfo.getTaskDetails()!=null) {
            taskToUpdate.setTitle(taskInfo.getTaskDetails().getTitle());
            taskToUpdate.setDescription(taskInfo.getTaskDetails().getDescription()!=null?
                    taskInfo.getTaskDetails().getDescription().getDescription():null);
        }
        if(taskInfo.getUpdate()!=null) {
            taskToUpdate.setUpdatedBy(taskInfo.getUpdate().getWho());
            taskToUpdate.setUpdatedDate(taskInfo.getUpdate().getWhen());
        }
        if(taskInfo.getBoardId()!=null) {
            taskToUpdate.setBoardId(taskInfo.getBoardId());
        }
        if(taskInfo.getStatus()!=null) {
            taskToUpdate.setStatus(taskInfo.getStatus());
        }
        return taskRepository.save(taskToUpdate);
    }
}
