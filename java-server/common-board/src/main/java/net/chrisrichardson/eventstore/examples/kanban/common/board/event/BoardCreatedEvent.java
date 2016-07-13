package net.chrisrichardson.eventstore.examples.kanban.common.board.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BoardCreatedEvent extends BoardEvent {
    @JsonUnwrapped
    private BoardInfo boardInfo;

    public BoardCreatedEvent() {
    }

    public BoardCreatedEvent(BoardInfo boardInfo) {
        this.boardInfo = boardInfo;
    }

    public BoardInfo getBoardInfo() {
        return boardInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
