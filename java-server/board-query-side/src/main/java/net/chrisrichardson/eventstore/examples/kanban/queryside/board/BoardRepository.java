package net.chrisrichardson.eventstore.examples.kanban.queryside.board;

import net.chrisrichardson.eventstore.examples.kanban.common.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by popikyardo on 21.09.15.
 */
public interface BoardRepository extends MongoRepository<Board, String> {
}
