package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service;

import io.eventuate.AggregateRepository;
import io.eventuate.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands.BoardCommand;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands.CreateBoardCommand;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class BoardService {

  private final AggregateRepository<BoardAggregate, BoardCommand> aggregateRepository;

  private static Logger log = LoggerFactory.getLogger(BoardService.class);

  public BoardService(AggregateRepository<BoardAggregate, BoardCommand> aggregateRepository) {
    this.aggregateRepository = aggregateRepository;
  }

  public CompletableFuture<EntityWithIdAndVersion<BoardAggregate>> save(BoardInfo board) {
    log.info("BoardService saving : {}", board);

    return aggregateRepository.save(new CreateBoardCommand(board));
  }
}
