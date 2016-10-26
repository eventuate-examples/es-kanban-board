package net.chrisrichardson.eventstore.examples.kanban.boardviewservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardviewservice.backend.BoardViewBackendConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BoardViewBackendConfiguration.class,
        EventuateDriverConfiguration.class,
        WebConfiguration.class,
        AuthConfiguration.class,
        CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
public class BoardViewServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(BoardViewServiceMain.class, args);
  }
}
