package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by popikyardo on 21.09.15.
 */
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
