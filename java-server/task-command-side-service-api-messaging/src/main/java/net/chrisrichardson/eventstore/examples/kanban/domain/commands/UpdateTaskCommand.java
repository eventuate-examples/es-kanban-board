package net.chrisrichardson.eventstore.examples.kanban.domain.commands;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskDetails;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateTaskCommand implements TaskCommand {
  private TaskDetails taskDetails;
  private AuditEntry update;

  public UpdateTaskCommand(TaskDetails taskDetails, AuditEntry update) {
    this.taskDetails = taskDetails;
    this.update = update;
  }

  public TaskDetails getTaskDetails() {
    return taskDetails;
  }

  public AuditEntry getUpdate() {
    return update;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}