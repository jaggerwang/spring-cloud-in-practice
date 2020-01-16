package net.jaggerwang.scip.gateway.api.config;

import org.springframework.beans.factory.annotation.Value;
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
    public WebClient postServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-post").build();
    }

    @Bean
    public WebClient fileServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-file").build();
    }

    @Bean
    public WebClient statServiceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("lb://spring-cloud-in-practice-stat").build();
    }

    @Bean
    public WebClient hydraServiceWebClient(@Value("${service.hydra.admin-url}") String adminUrl) {
        return WebClient.builder().baseUrl(adminUrl).build();
    }
}
