package net.chrisrichardson.eventstore.examples.kanban.boardviewservice.backend;

import net.chrisrichardson.eventstore.examples.kanban.common.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {
}
