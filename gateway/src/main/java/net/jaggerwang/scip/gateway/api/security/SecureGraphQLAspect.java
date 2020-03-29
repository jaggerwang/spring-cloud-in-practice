package net.jaggerwang.scip.gateway.api.security;

import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.exception.UnauthenticatedException;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Check authentication and authorization of all GraphQL datafetchers, and convert Mono returned
 * by datafetcher to CompletableFuture, as GraphQL not support reactor's Mono and Flux.
 */
@Component
@Aspect
public class SecureGraphQLAspect {
    @Around("allDataFetchers() && isInApplication()")
    public Object doSecurityCheck(ProceedingJoinPoint joinPoint) {
        var args = joinPoint.getArgs();
        var env = (DataFetchingEnvironment) args[0];
        return ReactiveSecurityContextHolder.getContext()
                .doOnSuccess(securityContext ->  {
                    var method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                    var permitAll = method.getAnnotation(PermitAll.class);
                    if (permitAll == null) {
                        var auth = securityContext.getAuthentication();
                        if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                                !auth.isAuthenticated()) {
                            throw new UnauthenticatedException("未认证");
                        }
                    }
                })
                .flatMap(securityContext -> {
                    Object result;
                    try {
                        result = joinPoint.proceed();
                    } catch (Throwable e) {
                        return Mono.error(new RuntimeException(e));
                    }
                    return result instanceof Mono ? (Mono) result : Mono.just(result);
                })
                .subscriberContext(context -> env.getContext())
                .toFuture();
    }

    @Pointcut("target(graphql.schema.DataFetcher)")
    private void allDataFetchers() {
    }

    @Pointcut("within(net.jaggerwang.scip.gateway.adapter.graphql..*)")
    private void isInApplication() {
    }
}
