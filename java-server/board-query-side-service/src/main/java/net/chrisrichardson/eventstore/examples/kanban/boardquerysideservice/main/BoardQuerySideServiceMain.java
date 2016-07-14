package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.BoardQuerySideServiceConfiguration;
import org.springframework.boot.SpringApplication;

public class BoardQuerySideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BoardQuerySideServiceConfiguration.class, args);
    }
}
