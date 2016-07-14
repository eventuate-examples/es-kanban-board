package net.chrisrichardson.eventstore.examples.kanban.common.board;

import org.apache.commons.lang.builder.ToStringBuilder;
import net.chrisrichardson.eventstore.examples.kanban.common.model.AuditEntry;

public class BoardInfo {
    private String title;
    private AuditEntry creation;
    private AuditEntry update;


    public BoardInfo() {
    }

    public BoardInfo(String title, AuditEntry creation, AuditEntry update) {
        this.title = title;
        this.creation = creation;
        this.update = update;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuditEntry getCreation() {
        return creation;
    }

    public void setCreation(AuditEntry creation) {
        this.creation = creation;
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
