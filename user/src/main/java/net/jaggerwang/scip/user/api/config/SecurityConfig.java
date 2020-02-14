package net.jaggerwang.scip.user.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt())
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/actuator/**", "/user/register", "/user/verifyPassword",
                                "/user/logged")
                        .permitAll()
                        .anyRequest()
                        .hasAuthority("SCOPE_user"));
    }
}
