package net.chrisrichardson.eventstore.examples.kanban.commonauth;

import net.chrisrichardson.eventstore.examples.kanban.commonauth.filter.StatelessAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
@ComponentScan("net.chrisrichardson.eventstore.examples.kanban.commonauth")
@EnableConfigurationProperties({AuthProperties.class})
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
