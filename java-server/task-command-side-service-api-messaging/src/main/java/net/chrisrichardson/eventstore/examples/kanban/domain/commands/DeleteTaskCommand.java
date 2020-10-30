package net.chrisrichardson.eventstore.examples.kanban.domain.commands;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;

public class DeleteTaskCommand implements TaskCommand {
  private AuditEntry update;

  public DeleteTaskCommand(AuditEntry update) {
    this.update = update;
  }

  public AuditEntry getUpdate() {
    return update;
  }
}
