package net.jaggerwang.scip.user.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/actuator/**", "/hydra/**", "/user/register", "/user/verifyPassword",
                        "/user/logged")
                .permitAll()
                .anyRequest()
                .hasAuthority("SCOPE_user")
                .and()
                .oauth2ResourceServer()
                .jwt();
    }
}
