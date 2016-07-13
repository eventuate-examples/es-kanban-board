package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
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

