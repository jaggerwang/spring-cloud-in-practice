package net.jaggerwang.scip.post.adapter.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import net.jaggerwang.scip.common.adapter.service.UserServiceImpl;
import net.jaggerwang.scip.common.adapter.api.interceptor.HeadersRelayRequestInterceptor;
import net.jaggerwang.scip.common.usecase.port.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class ServiceConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> cbFactoryCustomizer() {
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

    @LoadBalanced
    @Bean
    public RestTemplate userServiceRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://spring-cloud-in-practice-user")
                .interceptors(new HeadersRelayRequestInterceptor("X-User-Id"))
                .build();
    }

    @Bean
    public UserService userSyncService(@Qualifier("userServiceRestTemplate") RestTemplate restTemplate,
                                       CircuitBreakerFactory cbFactory,
                                       ObjectMapper objectMapper) {
        return new UserServiceImpl(restTemplate, cbFactory, objectMapper);
    }
}
