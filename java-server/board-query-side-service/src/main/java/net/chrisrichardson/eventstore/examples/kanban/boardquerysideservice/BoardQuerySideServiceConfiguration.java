package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.domain.BoardRepository;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.service.BoardQueryWorkflow;
import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.service.BoardUpdateService;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthSecurityConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan
@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories
@EnableEventHandlers
@Import({EventuateDriverConfiguration.class, WebConfiguration.class, AuthSecurityConfiguration.class, CommonSwaggerConfiguration.class})
public class BoardQuerySideServiceConfiguration {
  @Bean
  public BoardQueryWorkflow boardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
    return new BoardQueryWorkflow(boardInfoUpdateService);
  }

  @Bean
  public BoardUpdateService boardInfoUpdateService(BoardRepository boardRepository) {
    return new BoardUpdateService(boardRepository);
  }
}
