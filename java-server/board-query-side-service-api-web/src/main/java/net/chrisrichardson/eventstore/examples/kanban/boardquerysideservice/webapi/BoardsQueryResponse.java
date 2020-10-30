package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;

import java.util.List;

public class BoardsQueryResponse {

  private List<Board> boards;

  public BoardsQueryResponse() {
  }

  public BoardsQueryResponse(List<Board> boards) {
    this.boards = boards;
  }

  public List<Board> getBoards() {
    return boards;
  }

  public void setBoards(List<Board> boards) {
    this.boards = boards;
  }
}
