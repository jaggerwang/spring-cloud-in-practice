package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@EnableWebFluxSecurity
public class SecurityConfig {
    private ObjectMapper objectMapper;
    private ReactiveUserDetailsService userDetailsService;

    public SecurityConfig(ObjectMapper objectMapper, ReactiveUserDetailsService userDetailsService) {
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authManager(
            ) {
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
                        .authenticationEntryPoint((exchange, exception) -> {
                            if (exchange.getRequest().getHeaders().getAccept()
                                    .contains(MediaType.APPLICATION_JSON)) {
                                return responseJson(exchange, HttpStatus.UNAUTHORIZED,
                                        new RootDto("unauthenticated", "未认证"));
                            } else {
                                var response = exchange.getResponse();
                                response.setStatusCode(HttpStatus.FOUND);
                                response.getHeaders().setLocation(
                                        UriComponentsBuilder.fromPath("/login").build().toUri());
                                return response.writeWith(Flux.just(
                                        response.bufferFactory().wrap("".getBytes())));
                            }
                        })
                        .accessDeniedHandler((exchange, accessDeniedException) -> {
                            if (exchange.getRequest().getHeaders().getAccept()
                                    .contains(MediaType.APPLICATION_JSON)) {
                                return responseJson(exchange, HttpStatus.FORBIDDEN,
                                        new RootDto("unauthorized", "未授权"));
                            } else {
                                var response = exchange.getResponse();
                                response.setStatusCode(HttpStatus.FORBIDDEN);
                                return response.writeWith(Flux.just(
                                        response.bufferFactory().wrap("未授权".getBytes())));
                            }
                        })
                )
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/favicon.ico", "/*/actuator/**", "/login", "/logout",
                                "/auth/login", "/auth/logout", "/auth/logged",
                                "/user/register").permitAll()
                        .anyExchange().authenticated())
                .formLogin(formLogin -> {})
                .logout(logout -> {})
                .build();
    }
}
