package net.chrisrichardson.eventstore.examples.kanban.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;


@SpringBootApplication(exclude = {MongoRepositoriesAutoConfiguration.class, MongoAutoConfiguration.class})
@Import(ApiGatewayServiceConfiguration.class)
public class ApiGatewayServiceMain {
  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayServiceMain.class, args);
  }
}
