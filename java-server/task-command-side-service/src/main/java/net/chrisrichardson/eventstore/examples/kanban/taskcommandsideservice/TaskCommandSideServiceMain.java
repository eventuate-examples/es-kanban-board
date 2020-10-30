package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(TaskCommandSideServiceConfiguration.class)
@SpringBootApplication(exclude = {MongoRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class})
public class TaskCommandSideServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(TaskCommandSideServiceMain.class, args);
  }
}
