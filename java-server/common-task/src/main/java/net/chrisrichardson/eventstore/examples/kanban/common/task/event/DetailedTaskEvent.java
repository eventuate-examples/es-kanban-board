package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;

public interface DetailedTaskEvent {

    String getBoardId();

    AuditEntry getUpdate();
}
