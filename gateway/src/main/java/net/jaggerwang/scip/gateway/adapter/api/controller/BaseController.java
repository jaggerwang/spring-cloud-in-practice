package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.gateway.adapter.api.security.BindedOidcUser;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

/**
 * @author Jagger Wang
 */
abstract public class BaseController {
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    protected Mono<LoggedUser> loginUser(ServerWebExchange exchange, String username,
                                         String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(auth -> ReactiveSecurityContextHolder.getContext()
                        .defaultIfEmpty(new SecurityContextImpl())
                        .flatMap(securityContext -> exchange.getSession()
                                .map(session -> {
                                    securityContext.setAuthentication(auth);
                                    session.getAttributes().put(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME,
                                            securityContext);
                                    return (LoggedUser) auth.getPrincipal();
                                })));
    }

    protected Mono<LoggedUser> logoutUser(ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> exchange.getSession()
                        .flatMap(session -> {
                            var auth = securityContext.getAuthentication();
                            securityContext.setAuthentication(null);
                            session.getAttributes().remove(
                                    DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);

                            if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                                    !auth.isAuthenticated()) {
                                return Mono.empty();
                            }
                            return Mono.just((LoggedUser) auth.getPrincipal());
                        }));
    }

    protected Mono<LoggedUser> loggedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.empty();
                    }
                    var principal = auth.getPrincipal();
                    if (principal instanceof BindedOidcUser) {
                        var bindedOidcUser = (BindedOidcUser) principal;
                        return Mono.just(bindedOidcUser.getLoggedUser());
                    } else if (principal instanceof LoggedUser) {
                        return Mono.just((LoggedUser) auth.getPrincipal());
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
