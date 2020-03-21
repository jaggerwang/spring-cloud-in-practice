package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.ExecutionInput;
import graphql.spring.web.reactive.ExecutionInputCustomizer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Primary
public class CustomExecutionInputCustomizer implements ExecutionInputCustomizer {
    @Override
    public Mono<ExecutionInput> customizeExecutionInput(ExecutionInput executionInput,
                                                        ServerWebExchange serverWebExchange) {
        return Mono.subscriberContext()
                .map(context -> executionInput.transform(builder -> builder.context(context)));
    }
}
