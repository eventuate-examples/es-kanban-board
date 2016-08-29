package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskBacklogEvent extends TaskEvent {

    private AuditEntry update;

    public TaskBacklogEvent() {
    }

    public TaskBacklogEvent(AuditEntry update) {
        this.update = update;
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