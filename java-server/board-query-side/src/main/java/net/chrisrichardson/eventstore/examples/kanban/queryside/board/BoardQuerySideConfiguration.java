package net.chrisrichardson.eventstore.examples.kanban.queryside.board;


import net.chrisrichardson.eventstore.javaapi.consumer.EnableJavaEventHandlers;
import net.chrisrichardson.eventstore.subscriptions.config.EventStoreSubscriptionsConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by popikyardo on 21.09.15.
 */
@Configuration
@Import({EventStoreSubscriptionsConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
@EnableMongoRepositories
@EnableJavaEventHandlers
public class BoardQuerySideConfiguration {

    @Bean
    public BoardQueryWorkflow boardQueryWorkflow(BoardUpdateService boardInfoUpdateService) {
        return new BoardQueryWorkflow(boardInfoUpdateService);
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
