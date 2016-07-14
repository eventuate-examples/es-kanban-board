package net.chrisrichardson.eventstore.examples.kanban.common.board.model;

public class BoardQueryResponse {

    private Board data;

    public BoardQueryResponse() {}

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
