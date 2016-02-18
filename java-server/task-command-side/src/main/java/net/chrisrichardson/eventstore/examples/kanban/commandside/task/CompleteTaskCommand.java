package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by Main on 17.10.2015.
 */
public class CompleteTaskCommand implements TaskCommand {
    private String boardId;
    private AuditEntry update;

    public CompleteTaskCommand(String boardId, AuditEntry update) {
        this.boardId = boardId;
        this.update = update;
    }

    public String getBoardId() {
        return boardId;
    }

    public AuditEntry getUpdate() {
        return update;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
