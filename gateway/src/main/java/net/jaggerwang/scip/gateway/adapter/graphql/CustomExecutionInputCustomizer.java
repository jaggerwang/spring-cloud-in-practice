package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.ExecutionInput;
import graphql.spring.web.reactive.ExecutionInputCustomizer;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

@Component
@Primary
public class CustomExecutionInputCustomizer implements ExecutionInputCustomizer {
    @Override
    public Mono<ExecutionInput> customizeExecutionInput(ExecutionInput executionInput,
                                                        ServerWebExchange serverWebExchange) {
        return serverWebExchange.getSession()
                .map(session -> {
                    var securityContext = session.getAttributes().get(
                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
                    if (securityContext == null) {
                        securityContext = new SecurityContextImpl();
                        session.getAttributes().put(
                                DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, securityContext);
                    }
                    return securityContext;
                })
                .map(securityContext -> executionInput.transform(builder -> builder.context(
                        Context.of(ServerWebExchange.class, serverWebExchange,
                                SecurityContext.class, securityContext))));
    }
}
