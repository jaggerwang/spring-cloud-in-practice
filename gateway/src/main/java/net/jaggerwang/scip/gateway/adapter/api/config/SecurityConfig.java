package net.jaggerwang.scip.gateway.adapter.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

/**
 * @author Jagger Wang
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                HttpStatus.UNAUTHORIZED))
                )
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/", "/actuator/**", "/auth/**", "/files/**").permitAll()
                        .anyExchange().authenticated())
                .build();
    }
}
