package net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.boardquerysideservice.BoardQuerySideServiceConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Created by Main on 19.01.2016.
 */
public class BoardQuerySideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BoardQuerySideServiceConfiguration.class, args);
    }
}
