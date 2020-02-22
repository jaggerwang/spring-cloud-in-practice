package net.jaggerwang.scip.user.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import net.jaggerwang.scip.common.adapter.service.sync.FileSyncServiceImpl;
import net.jaggerwang.scip.common.adapter.service.sync.StatSyncServiceImpl;
import net.jaggerwang.scip.common.usecase.port.service.sync.FileSyncService;
import net.jaggerwang.scip.common.usecase.port.service.sync.StatSyncService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
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

    @LoadBalanced
    @Bean
    public RestTemplate fileServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://spring-cloud-in-practice-file").build();
    }

    @Bean
    @RequestScope
    public FileSyncService fileSyncService(@Qualifier("fileServiceRestTemplate") RestTemplate restTemplate,
                                           CircuitBreakerFactory cbFactory,
                                           ObjectMapper objectMapper,
                                           HttpServletRequest request) {
        return new FileSyncServiceImpl(restTemplate, cbFactory, objectMapper, request);
    }

    @LoadBalanced
    @Bean
    public RestTemplate statServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://spring-cloud-in-practice-stat").build();
    }

    @Bean
    @RequestScope
    public StatSyncService statSyncService(@Qualifier("statServiceRestTemplate") RestTemplate restTemplate,
                                           CircuitBreakerFactory cbFactory,
                                           ObjectMapper objectMapper,
                                           HttpServletRequest request) {
        return new StatSyncServiceImpl(restTemplate, cbFactory, objectMapper, request);
    }
}
