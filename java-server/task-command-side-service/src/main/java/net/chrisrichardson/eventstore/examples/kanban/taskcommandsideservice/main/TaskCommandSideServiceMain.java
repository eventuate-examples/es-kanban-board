package net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.taskcommandsideservice.TaskCommandSideServiceConfiguration;
import org.springframework.boot.SpringApplication;

public class TaskCommandSideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(TaskCommandSideServiceConfiguration.class, args);
    }
}
