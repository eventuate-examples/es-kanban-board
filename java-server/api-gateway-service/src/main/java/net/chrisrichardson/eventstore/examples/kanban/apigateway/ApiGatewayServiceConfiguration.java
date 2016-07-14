package net.chrisrichardson.eventstore.examples.kanban.apigateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eventuate.javaclient.spring.httpstomp.EventuateHttpStompClientConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebSocketConfig;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebSocketSecurityConfig;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.WebsocketEventsTranslator;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@ComponentScan
@Import({EventuateHttpStompClientConfiguration.class, WebConfiguration.class, AuthConfiguration.class, WebSocketConfig.class, WebSocketSecurityConfig.class})
@EnableConfigurationProperties({ApiGatewayProperties.class})
public class ApiGatewayServiceConfiguration {

    @Bean
    public WebsocketEventsTranslator websocketEventsTranslator(SimpMessagingTemplate template) {
        return new WebsocketEventsTranslator(template);
    }

    @Bean
    public RestTemplate restTemplate(HttpMessageConverters converters) {

        // we have to define Apache HTTP client to use the PATCH verb
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        converter.setObjectMapper(new ObjectMapper());

        HttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        restTemplate.setErrorHandler(new RestTemplateErrorHandler());

        return restTemplate;
    }
}
