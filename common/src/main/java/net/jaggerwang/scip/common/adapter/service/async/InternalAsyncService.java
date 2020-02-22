package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.InternalApiException;
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
import java.util.Objects;

public abstract class InternalAsyncService extends AsyncService {
    protected ObjectMapper objectMapper;

    public InternalAsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                                ObjectMapper objectMapper) {
        super(webClient, cbFactory);
        this.objectMapper = objectMapper;
    }

    private Mono<Map<String, Object>> handleResponse(ClientResponse response) {
        return response
                .bodyToMono(RootDto.class)
                .map(rootDto -> {
                    if (!Objects.equals(rootDto.getCode(), "ok"))
                        throw new InternalApiException(response.statusCode(),
                                rootDto.getCode(), rootDto.getMessage(),
                                rootDto.getData());

                    return rootDto.getData();
                })
                .switchIfEmpty(Mono.error(new InternalApiException(
                        response.statusCode(), "fail", response.statusCode().toString())));
    }

    public Mono<Map<String, Object>> getData(URI uri) {
        return get(uri, null, null).flatMap(this::handleResponse);
    }

    public Mono<Map<String, Object>> getData(String path) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return getData(uri);
    }

    public Mono<Map<String, Object>> getData(String path, MultiValueMap<String, String> params) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return getData(uri);
    }

    public Mono<Map<String, Object>> getData(String path, Map<String, String> params) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return getData(path, mvm);
    }

    public <T> Mono<Map<String, Object>> postData(URI uri, @Nullable T body) {
        return post(uri, null, body, null).flatMap(this::handleResponse);
    }

    public <T> Mono<Map<String, Object>> postData(String path, @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return postData(uri, body);
    }

    public <T> Mono<Map<String, Object>> postData(String path, MultiValueMap<String, String> params,
                                          @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return postData(uri, body);
    }

    public <T> Mono<Map<String, Object>> postData(String path, Map<String, String> params,
                                          @Nullable T body) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return postData(path, mvm, body);
    }
}
