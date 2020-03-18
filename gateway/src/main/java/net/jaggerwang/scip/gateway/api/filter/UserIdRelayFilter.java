package net.jaggerwang.scip.gateway.api.filter;

import net.jaggerwang.scip.gateway.api.security.LoggedUser;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserIdRelayFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();
                    if (auth instanceof UsernamePasswordAuthenticationToken) {
                        var loggedUser = (LoggedUser) auth.getPrincipal();
                        return chain.filter(exchange.mutate()
                                .request(exchange.getRequest()
                                        .mutate()
                                        .headers(headers -> headers
                                                .set("X-User-Id", loggedUser.getId().toString()))
                                        .build())
                                .build());
                    }
                    return chain.filter(exchange);
                });
    }
}
