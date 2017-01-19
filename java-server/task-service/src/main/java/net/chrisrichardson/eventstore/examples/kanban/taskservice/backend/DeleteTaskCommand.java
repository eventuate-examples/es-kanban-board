package net.chrisrichardson.eventstore.examples.kanban.taskservice.backend;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;

public class DeleteTaskCommand implements TaskCommand {
    private AuditEntry update;

    public DeleteTaskCommand(AuditEntry update) {
        this.update = update;
    }

    public AuditEntry getUpdate() {
        return update;
    }
}
