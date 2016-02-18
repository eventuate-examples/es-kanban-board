package net.chrisrichardson.eventstore.examples.kanban.common.task.model;

import java.util.List;

/**
 * Created by popikyardo on 23.10.15.
 */
public class BacklogResponse {

    private List<Task> tasks;

    private List<Task> backlog;

    public BacklogResponse() {}

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getBacklog() {
        return backlog;
    }

    public void setBacklog(List<Task> backlog) {
        this.backlog = backlog;
    }
}
