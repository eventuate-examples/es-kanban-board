package net.chrisrichardson.eventstore.examples.kanban.taskviewservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.taskviewservice.backend.TaskViewBackendConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TaskViewBackendConfiguration.class,
        EventuateDriverConfiguration.class,
        WebConfiguration.class,
        AuthConfiguration.class,
        CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
public class TaskViewServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(TaskViewServiceMain.class, args);
  }
}
