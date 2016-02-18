package net.chrisrichardson.eventstore.examples.kanban.commandside.task;

import net.chrisrichardson.eventstore.EventStore;
import net.chrisrichardson.eventstore.repository.AggregateRepository;
import net.chrisrichardson.eventstore.subscriptions.config.EventStoreSubscriptionsConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@Import({EventStoreSubscriptionsConfiguration.class})
@EnableAutoConfiguration(exclude = {MongoRepositoriesAutoConfiguration.class})
@ComponentScan
public class TaskCommandSideConfiguration {

    @Bean
    public AggregateRepository<TaskAggregate, TaskCommand> taskAggregateRepository(EventStore eventStore) {
        return new AggregateRepository<>(TaskAggregate.class, eventStore);
    }

    @Bean
    public TaskService taskService(AggregateRepository<TaskAggregate, TaskCommand> taskAggregateRepository) {
        return new TaskService(taskAggregateRepository);
    }

    @Bean
    public TaskHistoryService taskHistoryService(EventStore eventStore) {
        return new TaskHistoryService(eventStore);
    }

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }

}


