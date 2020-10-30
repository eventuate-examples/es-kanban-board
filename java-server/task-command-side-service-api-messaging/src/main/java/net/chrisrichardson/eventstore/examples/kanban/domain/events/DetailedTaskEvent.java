package net.chrisrichardson.eventstore.examples.kanban.domain.events;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.AuditEntry;

public interface DetailedTaskEvent {

  String getBoardId();

  AuditEntry getUpdate();
}
