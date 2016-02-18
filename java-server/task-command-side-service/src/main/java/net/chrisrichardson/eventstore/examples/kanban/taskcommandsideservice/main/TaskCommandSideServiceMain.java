package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.TaskCommandSideServiceConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Created by Main on 19.01.2016.
 */
public class TaskCommandSideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(TaskCommandSideServiceConfiguration.class, args);
    }
}
