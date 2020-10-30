package net.chrisrichardson.eventstore.examples.kanban.domain.commands;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MoveToBacklogTaskCommand implements TaskCommand {
  private AuditEntry update;

  public MoveToBacklogTaskCommand(AuditEntry update) {
    this.update = update;
  }

  public AuditEntry getUpdate() {
    return update;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
