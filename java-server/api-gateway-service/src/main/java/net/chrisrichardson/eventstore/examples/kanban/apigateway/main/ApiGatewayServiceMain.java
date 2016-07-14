package net.chrisrichardson.eventstore.examples.kanban.apigateway.main;

import net.chrisrichardson.eventstore.examples.kanban.apigateway.ApiGatewayServiceConfiguration;
import org.springframework.boot.SpringApplication;

public class ApiGatewayServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServiceConfiguration.class, args);
    }
}
