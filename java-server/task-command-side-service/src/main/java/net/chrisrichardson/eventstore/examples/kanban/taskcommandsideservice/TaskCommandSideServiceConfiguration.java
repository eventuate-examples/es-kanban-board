package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthSecurityConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.service.TaskAggregate;
import net.chrisrichardson.eventstore.examples.kanban.domain.commands.TaskCommand;
import net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.service.TaskHistoryService;
import net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.service.TaskService;
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
public class TaskCommandSideServiceConfiguration {
  @Bean
  public AggregateRepository<TaskAggregate, TaskCommand> taskAggregateRepository(EventuateAggregateStore eventStore) {
    return new AggregateRepository<>(TaskAggregate.class, eventStore);
  }

  @Bean
  public TaskService taskService(AggregateRepository<TaskAggregate, TaskCommand> taskAggregateRepository) {
    return new TaskService(taskAggregateRepository);
  }

  @Bean
  public TaskHistoryService taskHistoryService(EventuateAggregateStore eventStore) {
    return new TaskHistoryService(eventStore);
  }
}
