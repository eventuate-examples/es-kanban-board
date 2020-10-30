package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(TaskQuerySideServiceConfiguration.class)
@SpringBootApplication
public class TaskQuerySideServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(TaskQuerySideServiceMain.class, args);
  }
}
