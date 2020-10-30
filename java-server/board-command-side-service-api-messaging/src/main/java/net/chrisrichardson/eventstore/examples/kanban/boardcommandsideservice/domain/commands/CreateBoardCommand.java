package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import org.apache.commons.lang.builder.ToStringBuilder;

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
