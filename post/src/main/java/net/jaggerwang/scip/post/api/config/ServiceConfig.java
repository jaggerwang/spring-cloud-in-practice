package net.jaggerwang.scip.post.api.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public RestTemplate userServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("lb://spring-cloud-in-practice-user").build();
    }
}
