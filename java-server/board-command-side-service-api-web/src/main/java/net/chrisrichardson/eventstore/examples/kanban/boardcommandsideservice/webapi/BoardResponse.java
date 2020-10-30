package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.webapi;

import java.util.Date;

public class BoardResponse {
  private String id;
  private String title;
  private String createdBy;
  private Date createdDate;
  private String updatedBy;
  private Date updatedDate;

  public BoardResponse() {
  }

  public BoardResponse(String id, String title, String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
    this.id = id;
    this.title = title;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.updatedBy = updatedBy;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
