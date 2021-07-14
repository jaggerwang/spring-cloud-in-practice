package net.jaggerwang.scip.gateway.adapter.api.filter;

import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Jagger Wang
 */
@Component
public class UserIdGatewayGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.just(new SecurityContextImpl()))
                .flatMap(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return chain.filter(exchange);
                    }

                    var loggedUser = (LoggedUser) auth.getPrincipal();
                    return chain.filter(exchange.mutate()
                            .request(exchange.getRequest()
                                    .mutate()
                                    .headers(headers -> headers
                                            .set("X-User-Id", loggedUser.getId().toString()))
                                    .build())
                            .build());
                });
    }
}
