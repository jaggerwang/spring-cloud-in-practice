package net.jaggerwang.scip.gateway.adapter.api.filter;

import net.jaggerwang.scip.gateway.adapter.api.security.BindedOidcUser;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
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
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return chain.filter(exchange);
                    }

                    LoggedUser loggedUser;
                    var principal = auth.getPrincipal();
                    if (principal instanceof BindedOidcUser) {
                        var bindedOidcUser = (BindedOidcUser) principal;
                        loggedUser = bindedOidcUser.getLoggedUser();
                    } else {
                        loggedUser = (LoggedUser) principal;
                    }
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
