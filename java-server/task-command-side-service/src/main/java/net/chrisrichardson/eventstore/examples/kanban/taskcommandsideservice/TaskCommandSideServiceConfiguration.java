package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commandside.task.TaskCommandSideConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthSecurityConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({TaskCommandSideConfiguration.class, EventuateDriverConfiguration.class, WebConfiguration.class, AuthSecurityConfiguration.class, CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
public class TaskCommandSideServiceConfiguration {
}
