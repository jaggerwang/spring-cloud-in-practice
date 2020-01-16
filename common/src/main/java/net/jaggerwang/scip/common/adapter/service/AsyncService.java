package net.jaggerwang.scip.common.adapter.service;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public abstract class AsyncService {
    private WebClient webClient;
    private ReactiveCircuitBreakerFactory cbFactory;

    public AsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory) {
        this.webClient = webClient;
        this.cbFactory = cbFactory;
    }

    abstract public Optional<String> getCircuitBreakerName(URI uri);

    public Mono<ClientResponse> get(URI uri) {
        var u = new DefaultUriBuilderFactory().builder()
                .path(uri.getPath())
                .query(uri.getQuery())
                .build();
        var response = webClient.get().uri(u).exchange();

        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return response.transform(it -> cbFactory.create(cbName.get()).run(it));
        } else {
            return response;
        }
    }

    public Mono<ClientResponse> get(String path) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return get(uri);
    }

    public Mono<ClientResponse> get(String path, MultiValueMap<String, String> params) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return get(uri);
    }

    public Mono<ClientResponse> get(String path, Map<String, String> params) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return get(path, mvm);
    }

    public <T> Mono<ClientResponse> post(URI uri, @Nullable T body) {
        var u = new DefaultUriBuilderFactory().builder()
                .path(uri.getPath())
                .query(uri.getQuery())
                .build();
        var response = webClient.post().uri(u).bodyValue(body).exchange();

        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return response.transform(it -> cbFactory.create(cbName.get()).run(it));
        } else {
            return response;
        }
    }

    public <T> Mono<ClientResponse> post(String path, @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return post(uri, body);
    }

    public <T> Mono<ClientResponse> post(String path, MultiValueMap<String, String> params,
                                          @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return post(uri, body);
    }

    public <T> Mono<ClientResponse> post(String path, Map<String, String> params,
                                          @Nullable T body) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return post(path, mvm, body);
    }

    public <T> Mono<ClientResponse> put(URI uri, @Nullable T body) {
        var u = new DefaultUriBuilderFactory().builder()
                .path(uri.getPath())
                .query(uri.getQuery())
                .build();
        var response = webClient.put().uri(u).bodyValue(body).exchange();

        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return response.transform(it -> cbFactory.create(cbName.get()).run(it));
        } else {
            return response;
        }
    }

    public <T> Mono<ClientResponse> put(String path, @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return put(uri, body);
    }

    public <T> Mono<ClientResponse> put(String path, MultiValueMap<String, String> params,
                                         @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return put(uri, body);
    }

    public <T> Mono<ClientResponse> put(String path, Map<String, String> params,
                                         @Nullable T body) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return put(path, mvm, body);
    }
}
