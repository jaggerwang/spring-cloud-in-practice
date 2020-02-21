package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import net.jaggerwang.scip.common.adapter.service.async.*;
import net.jaggerwang.scip.common.api.filter.AuthHeaderRelayFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class ServiceConfig {
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> cbFactoryCustomizer() {
        return factory -> factory.configureDefault(id -> {
            var timeout = Duration.ofSeconds(2);
            if (id.equals("fast")) {
                timeout = Duration.ofSeconds(1);
            } else if (id.equals("slow")) {
                timeout = Duration.ofSeconds(5);
            }

            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom()
                            .timeoutDuration(timeout)
                            .build())
                    .build();
        });
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(new AuthHeaderRelayFilter());
    }

    @Bean
    public UserAsyncServiceImpl userAsyncService(WebClient.Builder builder,
                                                 ReactiveCircuitBreakerFactory cbFactory,
                                                 ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-user").build();
        return new UserAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public PostAsyncServiceImpl postAsyncService(WebClient.Builder builder,
                                                 ReactiveCircuitBreakerFactory cbFactory,
                                                 ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-post").build();
        return new PostAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public FileAsyncServiceImpl fileAsyncService(WebClient.Builder builder,
                                                 ReactiveCircuitBreakerFactory cbFactory,
                                                 ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-file").build();
        return new FileAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public StatAsyncServiceImpl statAsyncService(WebClient.Builder builder,
                                                 ReactiveCircuitBreakerFactory cbFactory,
                                                 ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-stat").build();
        return new StatAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public HydraAsyncServiceImpl hydraAsyncService(@Value("${service.hydra.admin-url}") String baseUrl,
                                                   ReactiveCircuitBreakerFactory cbFactory) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        return new HydraAsyncServiceImpl(webClient, cbFactory);
    }
}
