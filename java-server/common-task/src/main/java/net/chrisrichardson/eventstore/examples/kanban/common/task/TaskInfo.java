package net.chrisrichardson.eventstore.examples.kanban.common.task;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Created by popikyardo on 21.09.15.
 */
public class TaskInfo {
    private String boardId;
    private TaskDetails taskDetails;
    private AuditEntry creation;
    private AuditEntry update;
    private TaskStatus status;
    private boolean deleted;

    public TaskInfo() {
    }

    public TaskInfo(String boardId, TaskDetails taskDetails, AuditEntry creation, AuditEntry update, TaskStatus status, boolean deleted) {
        this.boardId = boardId;
        this.taskDetails = taskDetails;
        this.creation = creation;
        this.update = update;
        this.status = status;
        this.deleted = deleted;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public TaskDetails getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(TaskDetails taskDetails) {
        this.taskDetails = taskDetails;
    }

    public AuditEntry getCreation() {
        return creation;
    }

    public void setCreation(AuditEntry creation) {
        this.creation = creation;
    }

    public AuditEntry getUpdate() {
        return update;
    }

    public void setUpdate(AuditEntry update) {
        this.update = update;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
