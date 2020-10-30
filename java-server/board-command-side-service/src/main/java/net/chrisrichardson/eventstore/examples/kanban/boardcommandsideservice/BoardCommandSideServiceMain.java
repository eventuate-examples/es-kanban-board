package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {MongoRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class})
@Import(BoardCommandSideServiceConfiguration.class)
public class BoardCommandSideServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(BoardCommandSideServiceMain.class, args);
  }
}
