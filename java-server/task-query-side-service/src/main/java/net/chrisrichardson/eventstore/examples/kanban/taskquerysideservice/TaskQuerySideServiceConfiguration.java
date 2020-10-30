package net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonauth.AuthSecurityConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.domain.TaskRepository;
import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.service.TaskQueryWorkflow;
import net.chrisrichardson.eventstore.examples.kanban.taskquerysideservice.service.TaskUpdateService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Main on 19.01.2016.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableMongoRepositories
@EnableEventHandlers
@Import({EventuateDriverConfiguration.class, WebConfiguration.class, AuthSecurityConfiguration.class, CommonSwaggerConfiguration.class})
public class TaskQuerySideServiceConfiguration {


  @Bean
  public TaskQueryWorkflow taskQueryWorkflow(TaskUpdateService taskUpdateService) {
    return new TaskQueryWorkflow(taskUpdateService);
  }

  @Bean
  public TaskUpdateService taskUpdateService(TaskRepository taskRepository) {
    return new TaskUpdateService(taskRepository);
  }

  @Bean
  public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
    MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
    try {
      mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
    } catch (NoSuchBeanDefinitionException ignore) {
    }

    // Don't save _class to mongo
    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

    return mappingConverter;
  }
}
