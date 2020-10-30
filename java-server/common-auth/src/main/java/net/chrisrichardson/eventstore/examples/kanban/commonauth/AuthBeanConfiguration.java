package net.chrisrichardson.eventstore.examples.kanban.commonauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;

import java.security.SecureRandom;

@Configuration
@ComponentScan("net.chrisrichardson.eventstore.examples.kanban.commonauth")
@Import(AuthPropertiesConfiguration.class)
public class AuthBeanConfiguration {

  @Autowired
  private AuthProperties securityProperties;

  @Bean
  public TokenService tokenService() {
    KeyBasedPersistenceTokenService res = new KeyBasedPersistenceTokenService();
    res.setSecureRandom(new SecureRandom());
    res.setServerSecret(securityProperties.getServerSecret());
    res.setServerInteger(securityProperties.getServerInteger());

    return res;
  }
}
