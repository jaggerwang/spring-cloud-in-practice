package net.jaggerwang.scip.user.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.sync.FileSyncService;
import net.jaggerwang.scip.common.adapter.service.sync.StatSyncService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public RestTemplate fileServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://spring-cloud-in-practice-file").build();
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.sync.FileSyncService fileService(@Qualifier("fileServiceRestTemplate") RestTemplate restTemplate,
                                                                                            CircuitBreakerFactory cbFactory,
                                                                                            ObjectMapper objectMapper) {
        return new FileSyncService(restTemplate, cbFactory, objectMapper);
    }

    @Bean
    @LoadBalanced
    public RestTemplate statServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://spring-cloud-in-practice-stat").build();
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.sync.StatSyncService statService(@Qualifier("statServiceRestTemplate") RestTemplate restTemplate,
                                                                                            CircuitBreakerFactory cbFactory,
                                                                                            ObjectMapper objectMapper) {
        return new StatSyncService(restTemplate, cbFactory, objectMapper);
    }
}
