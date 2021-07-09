package net.jaggerwang.scip.gateway.adapter.api.filter;

import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
public class UserIdExchangeFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest,
                                       ExchangeFunction exchangeFunction) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return exchangeFunction.exchange(clientRequest);
                    }

                    var loggedUser = (LoggedUser) auth.getPrincipal();
                    return exchangeFunction.exchange(ClientRequest.from(clientRequest)
                            .headers(headers -> headers
                                    .set("X-User-Id", loggedUser.getId().toString()))
                            .build());
                });
    }
}
