package net.chrisrichardson.eventstore.examples.kanban.commandside.board;

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
public class BoardCommandSideConfiguration {

    @Bean
    public AggregateRepository<BoardAggregate, BoardCommand> boardAggregateRepository(EventStore eventStore) {
        return new AggregateRepository<>(BoardAggregate.class, eventStore);
    }

    @Bean
    public BoardService boardService(AggregateRepository<BoardAggregate, BoardCommand> boardAggregateRepository) {
        return new BoardService(boardAggregateRepository);
    }

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }

}


