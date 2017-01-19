package net.chrisrichardson.eventstore.examples.kanban.taskviewservice.web;

import net.chrisrichardson.eventstore.examples.kanban.taskviewservice.backend.TaskViewBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TaskViewBackendConfiguration.class)
@ComponentScan
public class TaskViewWebConfiguration {
}
