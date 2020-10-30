package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.service;

import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.domain.BoardRepository;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;

public class BoardUpdateService {
  private BoardRepository boardRepository;

  public BoardUpdateService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board create(String boardId, BoardInfo boardInfo) {
    return boardRepository.save(new Board(boardId,
            boardInfo.getTitle(),
            boardInfo.getCreation().getWho(),
            boardInfo.getCreation().getWhen(),
            boardInfo.getUpdate().getWhen(),
            boardInfo.getUpdate().getWho()));
  }
}
