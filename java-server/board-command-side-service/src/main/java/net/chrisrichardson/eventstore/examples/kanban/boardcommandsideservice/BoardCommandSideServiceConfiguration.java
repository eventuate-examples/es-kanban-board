package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service.BoardAggregate;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.domain.commands.BoardCommand;
import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.service.BoardService;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthSecurityConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@EnableEventHandlers
@Import({EventuateDriverConfiguration.class, WebConfiguration.class, AuthSecurityConfiguration.class, CommonSwaggerConfiguration.class})
public class BoardCommandSideServiceConfiguration {
  @Bean
  public AggregateRepository<BoardAggregate, BoardCommand> boardAggregateRepository(EventuateAggregateStore eventStore) {
    return new AggregateRepository<>(BoardAggregate.class, eventStore);
  }

  @Bean
  public BoardService boardService(AggregateRepository<BoardAggregate, BoardCommand> boardAggregateRepository) {
    return new BoardService(boardAggregateRepository);
  }
}
