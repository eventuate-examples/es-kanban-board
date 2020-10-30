package net.chrisrichardson.eventstore.examples.kanban.webapi;

public class ChangeTaskStatusRequest {
  private String boardId;

  public ChangeTaskStatusRequest() {
  }

  public ChangeTaskStatusRequest(String boardId) {
    this.boardId = boardId;
  }

  public String getBoardId() {
    return boardId;
  }

  public void setBoardId(String boardId) {
    this.boardId = boardId;
  }
}
