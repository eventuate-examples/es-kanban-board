package net.chrisrichardson.eventstore.examples.kanban.commandside.board;

import net.chrisrichardson.eventstore.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.kanban.common.board.BoardInfo;
import net.chrisrichardson.eventstore.examples.kanban.common.board.model.BoardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Main on 02.10.2015.
 */
@RestController
@RequestMapping(value = "/api")
public class BoardCommandController {

    @Autowired
    private BoardService boardService;

    private static Logger log = LoggerFactory.getLogger(BoardCommandController.class);

    @RequestMapping(value = "/boards", method = POST)
    public Observable<BoardResponse> saveBoard(@RequestBody BoardInfo board) {
        return boardService.save(board).map(this::makeBoardResponse);
    }

    private BoardResponse makeBoardResponse(EntityWithIdAndVersion<BoardAggregate> e) {
        return new BoardResponse(e.getEntityIdentifier().getId(),
                e.entity().getBoard().getTitle(),
                e.entity().getBoard().getCreation().getWho(),
                e.entity().getBoard().getCreation().getWhen(),
                e.entity().getBoard().getUpdate().getWho(),
                e.entity().getBoard().getUpdate().getWhen());
    }
}
