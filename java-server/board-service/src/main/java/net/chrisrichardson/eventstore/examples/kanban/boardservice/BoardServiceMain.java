package net.chrisrichardson.eventstore.examples.kanban.boardservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardservice.web.BoardWebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BoardWebConfiguration.class,
        EventuateDriverConfiguration.class,
        WebConfiguration.class,
        AuthConfiguration.class,
        CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
public class BoardServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(BoardServiceMain.class, args);
  }
}
