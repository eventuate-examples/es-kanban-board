package net.chrisrichardson.eventstore.examples.kanban.domain.commands;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CreateTaskCommand implements TaskCommand {
  private TaskInfo taskInfo;

  public CreateTaskCommand(TaskInfo taskInfo) {
    this.taskInfo = taskInfo;
  }

  public TaskInfo getTaskInfo() {
    return taskInfo;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

