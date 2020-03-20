package net.jaggerwang.scip.gateway.api.security;

import net.jaggerwang.scip.common.usecase.exception.UnauthenticatedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Aspect
@Order(1)
public class SecureGraphQLAspect {
    @Before("allDataFetchers() && isInApplication() && !isPermitAll()")
    public Mono<Void> doSecurityCheck() {
        return ReactiveSecurityContextHolder.getContext()
                .doOnSuccess(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        throw new UnauthenticatedException("未认证");
                    }
                })
                .then();
    }

    @Pointcut("target(graphql.schema.DataFetcher)")
    private void allDataFetchers() {
    }

    @Pointcut("within(net.jaggerwang.scip.gateway.adapter.graphql..*)")
    private void isInApplication() {
    }

    @Pointcut("@annotation(net.jaggerwang.scip.gateway.api.security.annotation.PermitAll)")
    private void isPermitAll() {
    }
}
