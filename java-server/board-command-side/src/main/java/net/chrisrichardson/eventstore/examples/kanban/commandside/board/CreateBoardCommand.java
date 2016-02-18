package net.chrisrichardson.eventstore.examples.kanban.commandside.board;

import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by popikyardo on 21.09.15.
 */
public class CreateBoardCommand implements BoardCommand {
    private BoardInfo boardInfo;

    public CreateBoardCommand(BoardInfo boardInfo) {
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
