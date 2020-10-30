package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.webapi;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.Task;

import java.util.List;

public class BacklogResponse {

  private List<Task> tasks;

  private List<Task> backlog;

  public BacklogResponse() {
  }

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
