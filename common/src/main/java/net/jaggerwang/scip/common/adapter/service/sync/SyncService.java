package net.jaggerwang.scip.common.adapter.service.sync;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

public abstract class SyncService {
    private RestTemplate restTemplate;
    private CircuitBreakerFactory cbFactory;

    public SyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory) {
        this.restTemplate = restTemplate;
        this.cbFactory = cbFactory;
    }

    abstract public Optional<String> getCircuitBreakerName(URI uri);

    public <T> ResponseEntity<T> get(URI uri, @Nullable HttpEntity<?> requestEntity,
                                     Class<T> responseType,
                                     @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate
                                    .exchange(u, HttpMethod.GET, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.GET, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.GET, requestEntity, responseType);
        }
    }

    public <T> ResponseEntity<T> get(URI uri, @Nullable HttpEntity<?> requestEntity,
                                     ParameterizedTypeReference<T> responseType,
                                     @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate
                                    .exchange(u, HttpMethod.GET, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.GET, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.GET, requestEntity, responseType);
        }
    }

    public <T> ResponseEntity<T> post(URI uri, @Nullable HttpEntity<?> requestEntity,
                                         Class<T> responseType,
                                         @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate
                                    .exchange(u, HttpMethod.POST, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.POST, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.POST, requestEntity, responseType);
        }
    }

    public <T> ResponseEntity<T> post(URI uri, @Nullable HttpEntity<?> requestEntity,
                                         ParameterizedTypeReference<T> responseType,
                                         @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate
                                    .exchange(u, HttpMethod.POST, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.POST, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.POST, requestEntity, responseType);
        }
    }

    public <T> ResponseEntity<T> put(URI uri, @Nullable HttpEntity<?> requestEntity,
                                        Class<T> responseType,
                                        @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate.
                                    exchange(u, HttpMethod.PUT, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.PUT, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.PUT, requestEntity, responseType);
        }
    }

    public <T> ResponseEntity<T> put(URI uri, @Nullable HttpEntity<?> requestEntity,
                                        ParameterizedTypeReference<T> responseType,
                                        @Nullable Function<Throwable, ResponseEntity<T>> fallback) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            var cb = cbFactory.create(cbName.get());
            return fallback != null ?
                    cb.run(() -> restTemplate
                                    .exchange(u, HttpMethod.PUT, requestEntity, responseType),
                            fallback) :
                    cb.run(() -> restTemplate
                            .exchange(u, HttpMethod.PUT, requestEntity, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.PUT, requestEntity, responseType);
        }
    }
}
