package net.jaggerwang.scip.gateway.adapter.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jagger Wang
 */
@Configuration(proxyBeanMethods = false)
public class RouteConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/auth/**")
                        .filters(f -> f.rewritePath("^/auth", ""))
                        .uri("lb://spring-cloud-in-practice-auth"))
                .route(p -> p.path("/user/**")
                        .filters(f -> f.rewritePath("^/user", ""))
                        .uri("lb://spring-cloud-in-practice-user"))
                .route(p -> p.path("/post/**")
                        .filters(f -> f.rewritePath("^/post", ""))
                        .uri("lb://spring-cloud-in-practice-post"))
                .route(p -> p.path("/file/**", "/files/**")
                        .filters(f -> f.rewritePath("^/file", ""))
                        .uri("lb://spring-cloud-in-practice-file"))
                .route(p -> p.path("/stat/**")
                        .filters(f -> f.rewritePath("^/stat", ""))
                        .uri("lb://spring-cloud-in-practice-stat"))
                .build();
    }
}
