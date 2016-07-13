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
@EnableWebSecurity
@EnableConfigurationProperties({AuthProperties.class})
public class AuthConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProperties securityProperties;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().loginPage("/index.html").and()
                .authorizeRequests()
                .antMatchers("/health").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/views/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/configuration/**").permitAll()
                .antMatchers("/validatorUrl/**").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/events/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/events").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TokenService tokenService() {
        KeyBasedPersistenceTokenService res = new KeyBasedPersistenceTokenService();
        res.setSecureRandom(new SecureRandom());
        res.setServerSecret(securityProperties.getServerSecret());
        res.setServerInteger(securityProperties.getServerInteger());

        return res;
    }
}
