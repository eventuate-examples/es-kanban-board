package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.web;

import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.domain.BoardRepository;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardQueryResponse;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.webapi.BoardsQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class BoardQueryController {

  @Autowired
  private BoardRepository boardRepository;

  @RequestMapping(value = "api/boards", method = GET)
  public ResponseEntity<BoardsQueryResponse> listAllBoards() {
    return new ResponseEntity<>(new BoardsQueryResponse(boardRepository.findAll()), OK);
  }

  @RequestMapping(value = "api/boards/{id}", method = GET)
  public ResponseEntity<BoardQueryResponse> getBoard(@PathVariable("id") String id) {
    return boardRepository.findById(id)
            .map(b -> new ResponseEntity<>(new BoardQueryResponse(b), OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
