package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.async.*;
import net.jaggerwang.scip.common.usecase.port.service.async.*;
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
    public UserService userService(WebClient.Builder builder,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("lb://spring-cloud-in-practice-user").build();
        return new UserAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public PostService postService(WebClient.Builder builder,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("lb://spring-cloud-in-practice-post").build();
        return new PostAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public FileService fileService(WebClient.Builder builder,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("lb://spring-cloud-in-practice-file").build();
        return new FileAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public StatService statService(WebClient.Builder builder,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        var webClient = builder.baseUrl("lb://spring-cloud-in-practice-stat").build();
        return new StatAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public HydraService hydraService(@Value("${service.hydra.admin-url}") String baseUrl,
                                     ReactiveCircuitBreakerFactory cbFactory,
                                     ObjectMapper objectMapper) {
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        return new HydraAsyncService(webClient, cbFactory, objectMapper);
    }
}
