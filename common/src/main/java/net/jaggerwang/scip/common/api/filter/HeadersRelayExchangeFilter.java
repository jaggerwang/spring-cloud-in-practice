package net.jaggerwang.scip.common.api.filter;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class HeadersRelayExchangeFilter implements ExchangeFilterFunction {
    private List<String> headers;

    public HeadersRelayExchangeFilter(String... headers) {
        this.headers = Arrays.asList(headers);
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest,
                                       ExchangeFunction exchangeFunction) {
        return Mono.subscriberContext()
                .flatMap(context -> {
                    var request = clientRequest;
                    var upstreamExchange = context.getOrEmpty(ServerWebExchange.class);
                    if (upstreamExchange.isPresent()) {
                        var builder = ClientRequest.from(request);
                        var upstreamHeaders = ((ServerWebExchange) upstreamExchange.get())
                                .getRequest().getHeaders();
                        for (var header: headers) {
                            if (upstreamHeaders.get(header) != null) {
                                builder.header(header,
                                        upstreamHeaders.get(header).toArray(new String[0]));
                            }
                        }
                        request = builder.build();
                    }
                    return exchangeFunction.exchange(request);
                });
    }
}
