package net.chrisrichardson.eventstore.examples.kanban.boardviewservice.web;

import net.chrisrichardson.eventstore.examples.kanban.boardviewservice.backend.BoardViewBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BoardViewBackendConfiguration.class)
@ComponentScan
public class BoardViewWebConfiguration {
}
