package net.jaggerwang.scip.gateway.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    private TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory;

    public RouteConfig(TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory) {
        this.tokenRelayGatewayFilterFactory = tokenRelayGatewayFilterFactory;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/user/**")
                        .filters(f -> f.filters(tokenRelayGatewayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-user"))
                .route(p -> p.path("/post/**")
                        .filters(f -> f.filters(tokenRelayGatewayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-post"))
                .route(p -> p.path("/file/**").or().path("/files/**")
                        .filters(f -> f.filters(tokenRelayGatewayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-file"))
                .route(p -> p.path("/stat/**")
                        .filters(f -> f.filters(tokenRelayGatewayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-stat"))
                .build();
    }
}
