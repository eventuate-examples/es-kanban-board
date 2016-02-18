package net.chrisrichardson.eventstore.examples.kanban.standalone;

import net.chrisrichardson.eventstore.client.config.EventStoreHttpClientConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commandside.board.BoardCommandSideConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commandside.task.TaskCommandSideConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebSocketConfig;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebSocketSecurityConfig;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebsocketEventsTranslator;
import net.chrisrichardson.eventstore.examples.kanban.queryside.board.BoardQuerySideConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.queryside.task.TaskQuerySideConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by Main on 06.10.2015.
 */
@Configuration
@Import({EventStoreHttpClientConfiguration.class, WebConfiguration.class, AuthConfiguration.class, WebSocketConfig.class, WebSocketSecurityConfig.class, BoardQuerySideConfiguration.class, TaskQuerySideConfiguration.class, BoardCommandSideConfiguration.class, TaskCommandSideConfiguration.class, CommonSwaggerConfiguration.class})
@ComponentScan
public class StandaloneServiceConfiguration {
    @Bean
    public WebsocketEventsTranslator websocketEventsTranslator(SimpMessagingTemplate template) {
        return new WebsocketEventsTranslator(template);
    }
}