package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.DetailedTaskEvent;
import net.chrisrichardson.eventstore.examples.kanban.common.task.event.TaskEvent;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by Main on 17.10.2015.
 */
public class TaskStartedEvent extends TaskEvent implements DetailedTaskEvent {
    private String boardId;
    private AuditEntry update;

    public TaskStartedEvent() {
    }

    public TaskStartedEvent(String boardId, AuditEntry update) {
        this.boardId = boardId;
        this.update = update;
    }

    @Override
    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
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
