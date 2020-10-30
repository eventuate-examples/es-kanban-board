package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;

public class BoardQueryResponse {

  private Board data;

  public BoardQueryResponse() {
  }

  public BoardQueryResponse(Board data) {
    this.data = data;
  }

  public Board getData() {
    return data;
  }

  public void setData(Board data) {
    this.data = data;
  }
}
