package net.jaggerwang.scip.user.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {
    @Bean
    public RestTemplate hydraServiceRestTemplate(RestTemplateBuilder builder,
                                                 @Value("${service.hydra.admin-url}") String adminUrl) {
        return builder.rootUri(adminUrl).build();
    }
}
