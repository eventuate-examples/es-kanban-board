package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.web;

import io.eventuate.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service.BoardService;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service.BoardAggregate;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.webapi.BoardResponse;
import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.BoardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api")
public class BoardCommandController {

  @Autowired
  private BoardService boardService;

  private static Logger log = LoggerFactory.getLogger(BoardCommandController.class);

  @RequestMapping(value = "/boards", method = POST)
  public CompletableFuture<BoardResponse> saveBoard(@RequestBody BoardInfo board) {
    return boardService.save(board).thenApply(this::makeBoardResponse);
  }

  private BoardResponse makeBoardResponse(EntityWithIdAndVersion<BoardAggregate> e) {
    return new BoardResponse(e.getEntityId(),
            e.getAggregate().getBoard().getTitle(),
            e.getAggregate().getBoard().getCreation().getWho(),
            e.getAggregate().getBoard().getCreation().getWhen(),
            e.getAggregate().getBoard().getUpdate().getWho(),
            e.getAggregate().getBoard().getUpdate().getWhen());
  }
}
