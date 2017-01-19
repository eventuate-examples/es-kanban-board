package net.chrisrichardson.eventstore.examples.kanban.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.javaclient.spring.jdbc.EmbeddedTestAggregateStoreConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardservice.web.BoardWebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.boardviewservice.web.BoardViewWebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebsocketEventsTranslator;
import net.chrisrichardson.eventstore.examples.kanban.taskservice.web.TaskWebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.taskviewservice.web.TaskViewWebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.testutil.BasicWebTestConfiguration;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@Import({EmbeddedTestAggregateStoreConfiguration.class,
        BasicWebTestConfiguration.class,
        BoardWebConfiguration.class,
        TaskViewWebConfiguration.class,
        BoardViewWebConfiguration.class,
        TaskWebConfiguration.class})
public class RestAPITestConfiguration {

  @Bean
  public WebsocketEventsTranslator websocketEventsTranslator(SimpMessagingTemplate template) {
    return new WebsocketEventsTranslator(template);
  }

  @Bean
  public RestTemplate restTemplate() {
    // we have to define Apache HTTP client to use the PATCH verb
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
    converter.setObjectMapper(new ObjectMapper());

    HttpClient httpClient = HttpClients.createDefault();
    RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    return restTemplate;
  }
}
