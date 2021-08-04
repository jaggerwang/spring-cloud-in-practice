package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.gateway.adapter.api.security.BindedOidcUser;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import reactor.core.publisher.Mono;

/**
 * @author Jagger Wang
 */
abstract public class BaseController {
    protected Mono<LoggedUser> loggedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.empty();
                    }

                    LoggedUser loggedUser;
                    var principal = auth.getPrincipal();
                    if (principal instanceof BindedOidcUser) {
                        var bindedOidcUser = (BindedOidcUser) principal;
                        loggedUser = bindedOidcUser.getLoggedUser();
                    } else {
                        loggedUser = (LoggedUser) principal;
                    }
                    return Mono.just(loggedUser);
                });
    }
}
