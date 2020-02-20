package net.jaggerwang.scip.gateway.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {
    /**
     * TODO Right now it will not auto refresh access token when expired.
     * {@see https://github.com/spring-cloud/spring-cloud-security/issues/175}
     */
    private TokenRelayGatewayFilterFactory tokenRelayFilterFactory;

    public RouteConfig(TokenRelayGatewayFilterFactory tokenRelayFilterFactory) {
        this.tokenRelayFilterFactory = tokenRelayFilterFactory;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/user/**")
                        .filters(f -> f
                                .filters(tokenRelayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-user"))
                .route(p -> p.path("/post/**")
                        .filters(f -> f
                                .filters(tokenRelayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-post"))
                .route(p -> p.path("/file/**").or().path("/files/**")
                        .filters(f -> f
                                .filters(tokenRelayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-file"))
                .route(p -> p.path("/stat/**")
                        .filters(f -> f
                                .filters(tokenRelayFilterFactory.apply())
                                .removeRequestHeader("Cookie"))
                        .uri("lb://spring-cloud-in-practice-stat"))
                .build();
    }
}
