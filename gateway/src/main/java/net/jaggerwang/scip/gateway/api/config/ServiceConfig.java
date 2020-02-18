package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.async.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri("http://spring-cloud-in-practice-user").build();
    }

    @Bean
    @LoadBalanced
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://spring-cloud-in-practice-user").build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService userService(WebClient.Builder builder,
                                                                                              ReactiveCircuitBreakerFactory cbFactory,
                                                                                              ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-user").build();
        return new UserAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.async.PostAsyncService postService(WebClient.Builder builder,
                                                                                              ReactiveCircuitBreakerFactory cbFactory,
                                                                                              ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-post").build();
        return new PostAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.async.FileAsyncService fileService(WebClient.Builder builder,
                                                                                              ReactiveCircuitBreakerFactory cbFactory,
                                                                                              ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-file").build();
        return new FileAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.async.StatAsyncService statService(WebClient.Builder builder,
                                                                                              ReactiveCircuitBreakerFactory cbFactory,
                                                                                              ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-stat").build();
        return new StatAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public net.jaggerwang.scip.common.usecase.port.service.async.HydraAsyncService hydraService(@Value("${service.hydra.admin-url}") String baseUrl,
                                                                                                ReactiveCircuitBreakerFactory cbFactory) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        return new HydraAsyncService(webClient, cbFactory);
    }
}
