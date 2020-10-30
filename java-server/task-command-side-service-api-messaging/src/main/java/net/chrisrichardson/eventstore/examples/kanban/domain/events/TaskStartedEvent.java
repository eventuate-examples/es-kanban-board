package net.chrisrichardson.eventstore.examples.kanban.domain.events;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

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
