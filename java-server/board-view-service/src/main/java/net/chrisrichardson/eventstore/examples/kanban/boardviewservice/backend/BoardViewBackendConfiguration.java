package net.chrisrichardson.eventstore.examples.kanban.boardviewservice.backend;


import io.eventuate.javaclient.spring.EnableEventHandlers;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableEventHandlers
@EnableMongoRepositories
public class BoardViewBackendConfiguration {

    @Bean
    public BoardViewEventSubscriber boardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
        return new BoardViewEventSubscriber(boardInfoUpdateService);
    }

    @Bean
    public BoardUpdateService boardInfoUpdateService(BoardRepository boardRepository) {
        return new BoardUpdateService(boardRepository);
    }

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additional);
    }
}
