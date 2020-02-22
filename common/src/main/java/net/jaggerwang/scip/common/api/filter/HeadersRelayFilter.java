package net.jaggerwang.scip.common.api.filter;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

public class HeadersRelayFilter implements ExchangeFilterFunction {
    private Set<String> headers;

    public HeadersRelayFilter(Set<String> headers) {
        this.headers = headers;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest,
                                       ExchangeFunction exchangeFunction) {
        return Mono.subscriberContext()
                .flatMap(ctx -> {
                    var upstreamExchange = ctx.getOrEmpty(ServerWebExchange.class);
                    if (upstreamExchange.isPresent()) {
                        var upstreamHeaders = ((ServerWebExchange) upstreamExchange.get())
                                .getRequest().getHeaders();
                        for (var header: headers) {
                            clientRequest.headers().addAll(header, upstreamHeaders.get(header));
                        }
                    }

                    return exchangeFunction.exchange(clientRequest);
                });
    }
}
