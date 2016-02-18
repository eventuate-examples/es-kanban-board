package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.main;

import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.TaskQuerySideServiceConfiguration;
import org.springframework.boot.SpringApplication;

/**
 * Created by Main on 19.01.2016.
 */
public class TaskQuerySideServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(TaskQuerySideServiceConfiguration.class, args);
    }
}
