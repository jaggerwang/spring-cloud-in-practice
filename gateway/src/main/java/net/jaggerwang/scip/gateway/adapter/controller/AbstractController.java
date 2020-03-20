package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.gateway.api.security.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

abstract public class AbstractController {
    @Autowired
    private ReactiveAuthenticationManager authManager;

    protected Mono<ServerWebExchange> getServerWebExchange() {
        return Mono.subscriberContext()
                .map(context -> context.get(ServerWebExchange.class));
    }

    protected Mono<WebSession> getWebSession() {
        return getServerWebExchange()
                .flatMap(exchange -> exchange.getSession());
    }

    protected Mono<SecurityContext> getSecurityContext() {
        return getWebSession()
                .map(session -> {
                    var securityContext = session.getAttributes().get(
                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
                    if (securityContext == null) {
                        securityContext = new SecurityContextImpl();
                        session.getAttributes().put(
                                DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, securityContext);
                    }
                    return (SecurityContext) securityContext;
                });
    }

    protected Mono<LoggedUser> loginUser(String username, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(auth -> getSecurityContext()
                        .flatMap(context -> getWebSession()
                                .map(session -> {
                                    context.setAuthentication(auth);
                                    session.getAttributes().put(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, context);
                                    return (LoggedUser) auth.getPrincipal();
                                })));
    }

    protected Mono<LoggedUser> logoutUser() {
        return loggedUser()
                .flatMap(loggedUser -> getSecurityContext()
                        .flatMap(context -> getWebSession()
                                .map(session -> {
                                    context.setAuthentication(null);
                                    session.getAttributes().remove(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
                                    return loggedUser;
                                })));
    }

    protected Mono<LoggedUser> loggedUser() {
        return getSecurityContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.empty();
                    }
                    return Mono.just((LoggedUser) auth.getPrincipal());
                });
    }

    protected Mono<Long> loggedUserId() {
        return loggedUser()
                .map(loggedUser -> loggedUser.getId());
    }
}
