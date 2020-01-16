package net.jaggerwang.scip.user.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.sync.HydraSyncService;
import net.jaggerwang.scip.common.usecase.port.service.sync.HydraService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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

    @Bean
    public HydraService hydraService(@Qualifier("hydraServiceRestTemplate") RestTemplate restTemplate,
                                     CircuitBreakerFactory cbFactory,
                                     ObjectMapper objectMapper) {
        return new HydraSyncService(restTemplate, cbFactory, objectMapper);
    }
}
