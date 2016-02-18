package net.chrisrichardson.eventstore.examples.kanban.common.task.event;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;

/**
 * Created by popikyardo on 18.12.15.
 */
public interface DetailedTaskEvent {

    String getBoardId();

    AuditEntry getUpdate();
}
