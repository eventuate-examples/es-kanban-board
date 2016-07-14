package net.chrisrichardson.eventstore.examples.kanban.common.board.model;

import java.util.List;

public class BoardsQueryResponse {

    private List<Board> boards;

    public BoardsQueryResponse() {}

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
