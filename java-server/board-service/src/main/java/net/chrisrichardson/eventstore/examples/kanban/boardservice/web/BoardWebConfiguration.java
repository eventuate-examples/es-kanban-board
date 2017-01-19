package net.chrisrichardson.eventstore.examples.kanban.boardservice.web;

import net.chrisrichardson.eventstore.examples.kanban.boardservice.backend.BoardBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BoardBackendConfiguration.class)
@ComponentScan
public class BoardWebConfiguration {
}
