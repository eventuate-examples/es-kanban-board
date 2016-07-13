package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDetails;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskEvent;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskUpdatedEvent extends TaskEvent {
    private TaskDetails taskDetails;
    private AuditEntry update;

    public TaskUpdatedEvent() {
    }

    public TaskUpdatedEvent(TaskDetails taskDetails, AuditEntry update) {
        this.taskDetails = taskDetails;
        this.update = update;
    }

    public TaskDetails getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(TaskDetails taskDetails) {
        this.taskDetails = taskDetails;
    }

    public AuditEntry getUpdate() {
        return update;
    }

    public void setUpdate(AuditEntry update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
