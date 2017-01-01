package net.chrisrichardson.eventstore.examples.kanban.taskservice.web;

import net.chrisrichardson.eventstore.examples.kanban.taskservice.backend.TaskBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TaskBackendConfiguration.class)
@ComponentScan
public class TaskWebConfiguration {
}
