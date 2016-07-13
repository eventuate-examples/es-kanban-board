package net.chrisrichardson.eventstore.examples.kanban.common.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskDescription;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;

import java.util.Date;

public class TaskResponse {

    private String id;
    private String boardId;
    private String title;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private TaskStatus status;
    private boolean deleted;
    @JsonProperty("data")
    private TaskDescription description;

    public TaskResponse() {
    }

    public TaskResponse(String id, String boardId, String title, String createdBy, String updatedBy, Date createdDate, Date updatedDate, TaskStatus status, boolean deleted, TaskDescription description) {
        this.id = id;
        this.boardId = boardId;
        this.title = title;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.status = status;
        this.deleted = deleted;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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

    public TaskDescription getDescription() {
        return description;
    }

    public void setDescription(TaskDescription description) {
        this.description = description;
    }
}