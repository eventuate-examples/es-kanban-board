package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
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