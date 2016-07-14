package net.chrisrichardson.eventstore.examples.kanban.standalone.main;

import net.chrisrichardson.eventstore.examples.kanban.standalone.StandaloneServiceConfiguration;
import org.springframework.boot.SpringApplication;

public class StandaloneServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(StandaloneServiceConfiguration.class, args);
    }

}
