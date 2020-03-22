package net.jaggerwang.scip.gateway.api.security;

import net.jaggerwang.scip.common.usecase.exception.UnauthenticatedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;

// TODO: aspect not work with webflux right now.
//@Component
@Aspect
public class SecureGraphQLAspect {
    @Around("allDataFetchers() && isInApplication() && !isPermitAll()")
    public Object doSecurityCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        return ReactiveSecurityContextHolder.getContext()
                .doOnSuccess(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        throw new UnauthenticatedException("未认证");
                    }
                })
                .thenReturn(joinPoint.proceed());
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
