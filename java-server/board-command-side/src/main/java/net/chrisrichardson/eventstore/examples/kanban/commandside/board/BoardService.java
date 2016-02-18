package net.chrisrichardson.eventstore.examples.kanban.commandside.board;

import net.chrisrichardson.eventstore.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import net.chrisrichardson.eventstore.repository.AggregateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * Created by popikyardo on 21.09.15.
 */
public class BoardService {

    private final AggregateRepository<BoardAggregate, BoardCommand> aggregateRepository;

    private static Logger log = LoggerFactory.getLogger(BoardService.class);

    public BoardService(AggregateRepository<BoardAggregate, BoardCommand> aggregateRepository) {
        this.aggregateRepository = aggregateRepository;
    }

    public Observable<EntityWithIdAndVersion<BoardAggregate>> save(BoardInfo board) {
        log.info("BoardService saving : {}", board);

        return aggregateRepository.save(new CreateBoardCommand(board));
    }
}
