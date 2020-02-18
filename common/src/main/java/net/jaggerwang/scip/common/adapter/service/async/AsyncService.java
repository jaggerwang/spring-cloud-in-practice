package net.jaggerwang.scip.common.adapter.service.async;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

public abstract class AsyncService {
    private WebClient webClient;
    private ReactiveCircuitBreakerFactory cbFactory;

    public AsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory) {
        this.webClient = webClient;
        this.cbFactory = cbFactory;
    }

    abstract public Optional<String> getCircuitBreakerName(URI uri);

    public Mono<ClientResponse> get(URI uri,
                                    @Nullable Function<Throwable, Mono<ClientResponse>> fallback) {
        var response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(uri.getPath())
                        .query(uri.getQuery())
                        .build())
                .exchange();
        var cbName = getCircuitBreakerName(uri);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    response.transform(it -> cb.run(it, fallback)) :
                    response.transform(it -> cb.run(it));
        } else {
            return response;
        }
    }

    public <T> Mono<ClientResponse> post(URI uri, @Nullable T body,
                                         @Nullable Function<Throwable, Mono<ClientResponse>> fallback) {
        var response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(uri.getPath())
                        .query(uri.getQuery())
                        .build())
                .bodyValue(body)
                .exchange();
        var cbName = getCircuitBreakerName(uri);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    response.transform(it -> cb.run(it, fallback)) :
                    response.transform(it -> cb.run(it));
        } else {
            return response;
        }
    }

    public <T> Mono<ClientResponse> put(URI uri, @Nullable T body,
                                        @Nullable Function<Throwable, Mono<ClientResponse>> fallback) {
        var response = webClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path(uri.getPath())
                        .query(uri.getQuery())
                        .build())
                .bodyValue(body)
                .exchange();
        var cbName = getCircuitBreakerName(uri);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    response.transform(it -> cb.run(it, fallback)) :
                    response.transform(it -> cb.run(it));
        } else {
            return response;
        }
    }
}
