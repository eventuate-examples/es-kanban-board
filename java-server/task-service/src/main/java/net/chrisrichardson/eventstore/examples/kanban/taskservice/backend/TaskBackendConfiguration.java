package net.chrisrichardson.eventstore.examples.kanban.taskservice.backend;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.javaclient.spring.EnableEventHandlers;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableAutoConfiguration(exclude = {MongoRepositoriesAutoConfiguration.class})
@ComponentScan
@EnableEventHandlers
public class TaskBackendConfiguration {

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

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }

}


