package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice;

import io.eventuate.javaclient.spring.httpstomp.EventuateHttpStompClientConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.queryside.task.TaskQuerySideConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Main on 19.01.2016.
 */
@Configuration
@Import({TaskQuerySideConfiguration.class, EventuateHttpStompClientConfiguration.class, WebConfiguration.class, AuthConfiguration.class, CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
public class TaskQuerySideServiceConfiguration {
}
