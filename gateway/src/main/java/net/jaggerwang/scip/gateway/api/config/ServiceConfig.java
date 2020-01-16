package net.jaggerwang.scip.gateway.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.async.*;
import net.jaggerwang.scip.common.usecase.port.service.async.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-user").build();
    }

    @Bean
    public UserService userService(@Qualifier("userServiceWebClient") WebClient webClient,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        return new UserAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public WebClient postServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-post").build();
    }

    @Bean
    public PostService postService(@Qualifier("postServiceWebClient") WebClient webClient,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        return new PostAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public WebClient fileServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-file").build();
    }

    @Bean
    public FileService fileService(@Qualifier("fileServiceWebClient") WebClient webClient,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        return new FileAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public WebClient statServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-stat").build();
    }

    @Bean
    public StatService statService(@Qualifier("statServiceWebClient") WebClient webClient,
                                   ReactiveCircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        return new StatAsyncService(webClient, cbFactory, objectMapper);
    }

    @Bean
    public WebClient hydraServiceWebClient(@Value("${service.hydra.admin-url}") String adminUrl) {
        return WebClient.builder().baseUrl(adminUrl).build();
    }

    @Bean
    public HydraService hydraService(@Qualifier("hydraServiceWebClient") WebClient webClient,
                                     ReactiveCircuitBreakerFactory cbFactory,
                                     ObjectMapper objectMapper) {
        return new HydraAsyncService(webClient, cbFactory, objectMapper);
    }
}
