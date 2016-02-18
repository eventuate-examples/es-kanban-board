package net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.boardcommandsideservice.BoardCommandSideServiceConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Created by Main on 19.01.2016.
 */
public class BoardCommandSideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BoardCommandSideServiceConfiguration.class, args);
    }
}
