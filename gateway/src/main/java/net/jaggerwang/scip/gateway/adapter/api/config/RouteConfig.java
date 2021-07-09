package net.jaggerwang.scip.gateway.adapter.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/auth/**")
                        .uri("lb://spring-cloud-in-practice-auth"))
                .route(p -> p.path("/user/**")
                        .uri("lb://spring-cloud-in-practice-user"))
                .route(p -> p.path("/post/**")
                        .uri("lb://spring-cloud-in-practice-post"))
                .route(p -> p.path("/file/**", "/files/**")
                        .uri("lb://spring-cloud-in-practice-file"))
                .route(p -> p.path("/stat/**")
                        .uri("lb://spring-cloud-in-practice-stat"))
                .build();
    }
}
