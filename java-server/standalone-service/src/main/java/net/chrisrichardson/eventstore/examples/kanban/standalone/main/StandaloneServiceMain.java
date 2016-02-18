package net.chrisrichardson.eventstore.examples.kanban.standalone.main;

import net.chrisrichardson.eventstore.examples.kanban.standalone.StandaloneServiceConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Created by Main on 06.10.2015.
 */

public class StandaloneServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(StandaloneServiceConfiguration.class, args);
    }

}
