package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.gateway.api.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private ObjectMapper objectMapper;
    private ReactiveUserDetailsService userDetailsService;

    public SecurityConfig(ObjectMapper objectMapper, CustomUserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authManager() {
        var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder());
        return authManager;
    }

    private Mono<Void> responseJson(ServerWebExchange exchange, HttpStatus status, RootDto data) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        var body = new byte[0];
        try {
            body = objectMapper.writeValueAsBytes(data);
        } catch (IOException e) {
        }
        return response.writeWith(Flux.just(response.bufferFactory().wrap(body)));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authenticationManager(authManager())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, exception) ->
                                responseJson(exchange, HttpStatus.UNAUTHORIZED,
                                        new RootDto("unauthenticated", "未认证")))
                        .accessDeniedHandler((exchange, accessDeniedException) ->
                                responseJson(exchange, HttpStatus.FORBIDDEN,
                                        new RootDto("unauthorized", "未授权")))
                )
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/favicon.ico", "/*/actuator/**", "/", "/graphql", "/login",
                                "/logout", "/auth/login", "/auth/logout", "/auth/logged",
                                "/user/register").permitAll()
                        .anyExchange().authenticated())
                .formLogin(formLogin -> {})
                .logout(logout -> {})
                .build();
    }
}
