package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.domain;

import net.chrisrichardson.eventstore.examples.kanban.common.domain.board.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {
}
