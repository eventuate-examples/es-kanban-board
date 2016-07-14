package net.chrisrichardson.eventstore.examples.kanban.common.task.model;

import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.task.TaskStatus;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Task {
    @Id
    private String id;

    private String boardId;
    private String title;
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private TaskStatus status;
    private boolean deleted;
    private String description;

    public Task() {
    }

    public Task(String id, String boardId, String title, String createdBy, String updatedBy, Date createdDate, Date updatedDate, TaskStatus status, boolean deleted, String data) {
        this.id = id;
        this.boardId = boardId;
        this.title = title;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.status = status;
        this.deleted = deleted;
        this.description = data;
    }

    public static Task transform(String id, TaskInfo taskInfo) {
        Task res = new Task();
        res.setId(id);
        res.setBoardId(taskInfo.getBoardId());
        res.setTitle(taskInfo.getTaskDetails().getTitle());
        res.setCreatedBy(taskInfo.getCreation().getWho());
        res.setCreatedDate(taskInfo.getCreation().getWhen());
        res.setUpdatedDate(taskInfo.getUpdate().getWhen());
        res.setUpdatedBy(taskInfo.getUpdate().getWho());
        res.setStatus(taskInfo.getStatus());
        res.setDeleted(taskInfo.isDeleted());
        res.setDescription(taskInfo.getTaskDetails().getDescription()!=null?
                taskInfo.getTaskDetails().getDescription().getDescription() : null);
        return res;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
