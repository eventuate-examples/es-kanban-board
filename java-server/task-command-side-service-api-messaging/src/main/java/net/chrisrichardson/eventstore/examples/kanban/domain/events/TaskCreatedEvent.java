package net.chrisrichardson.eventstore.examples.kanban.domain.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.task.TaskInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskCreatedEvent extends TaskEvent {
  @JsonUnwrapped
  private TaskInfo taskInfo;

  public TaskCreatedEvent() {
  }

  public TaskCreatedEvent(TaskInfo taskInfo) {
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
