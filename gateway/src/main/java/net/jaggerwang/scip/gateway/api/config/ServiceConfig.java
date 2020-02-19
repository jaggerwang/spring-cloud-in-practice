package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.async.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public UserAsyncServiceImpl userService(WebClient.Builder builder,
                                            ReactiveCircuitBreakerFactory cbFactory,
                                            ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-user").build();
        return new UserAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public PostAsyncServiceImpl postService(WebClient.Builder builder,
                                            ReactiveCircuitBreakerFactory cbFactory,
                                            ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-post").build();
        return new PostAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public FileAsyncServiceImpl fileService(WebClient.Builder builder,
                                            ReactiveCircuitBreakerFactory cbFactory,
                                            ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-file").build();
        return new FileAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public StatAsyncServiceImpl statService(WebClient.Builder builder,
                                            ReactiveCircuitBreakerFactory cbFactory,
                                            ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("http://spring-cloud-in-practice-stat").build();
        return new StatAsyncServiceImpl(webClient, cbFactory, objectMapper);
    }

    @Bean
    public HydraAsyncServiceImpl hydraService(@Value("${service.hydra.admin-url}") String baseUrl,
                                              ReactiveCircuitBreakerFactory cbFactory) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        return new HydraAsyncServiceImpl(webClient, cbFactory);
    }
}
