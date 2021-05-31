package net.jaggerwang.scip.gateway.adapter.api.filter;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

@Component
public class ReactiveContextWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getSession()
                .flatMap(session -> {
                    session.getAttributes().putIfAbsent(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME,
                            new SecurityContextImpl());
                    return chain.filter(exchange)
                            .subscriberContext(context -> Context.of(
                                    ServerWebExchange.class, exchange,
                                    SecurityContext.class, Mono.just(session.getAttributes().get(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME))));
                });
    }
}
