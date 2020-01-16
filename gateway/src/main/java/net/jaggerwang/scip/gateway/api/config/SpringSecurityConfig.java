package net.jaggerwang.scip.gateway.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// TODO: not working for now, see more on https://github.com/spring-cloud/spring-cloud-gateway/issues/1523
//@Configuration
//@EnableWebFluxSecurity
public class SpringSecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/", "/actuator/**", "/hydra/**", "/graphql", "/user/register",
                                "/user/logged")
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                );
        return http.build();
    }
}
