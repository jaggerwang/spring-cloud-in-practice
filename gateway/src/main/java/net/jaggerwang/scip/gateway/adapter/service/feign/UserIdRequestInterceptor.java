package net.jaggerwang.scip.gateway.adapter.service.feign;

import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Jagger Wang
 */
public class UserIdRequestInterceptor implements ReactiveHttpRequestInterceptor {
    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .map(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return reactiveHttpRequest;
                    }

                    var loggedUser = (LoggedUser) auth.getPrincipal();
                    reactiveHttpRequest.headers().put("X-User-Id",
                            List.of(loggedUser.getId().toString()));
                    return reactiveHttpRequest;
                });
    }
}
