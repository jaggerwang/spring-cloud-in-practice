package net.jaggerwang.scip.common.adapter.service.sync;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public abstract class SyncService {
    private RestTemplate restTemplate;
    private CircuitBreakerFactory cbFactory;

    public SyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory) {
        this.restTemplate = restTemplate;
        this.cbFactory = cbFactory;
    }

    abstract public Optional<String> getCircuitBreakerName(URI uri);

    public <T> ResponseEntity<T> get(URI uri, Class<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.GET, null, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.GET, null, responseType);
        }
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return get(uri, responseType);
    }

    public <T> ResponseEntity<T> get(String path, MultiValueMap<String, String> params,
                                     Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return get(uri, responseType);
    }

    public <T> ResponseEntity<T> get(String path, Map<String, String> params,
                                     Class<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return get(path, mvm, responseType);
    }

    public <T> ResponseEntity<T> get(URI uri, ParameterizedTypeReference<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.GET, null, responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.GET, null, responseType);
        }
    }

    public <T> ResponseEntity<T> get(String path, ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return get(uri, responseType);
    }

    public <T> ResponseEntity<T> get(String path, MultiValueMap<String, String> params,
                                     ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return get(uri, responseType);
    }

    public <T> ResponseEntity<T> get(String path, Map<String, String> params,
                                     ParameterizedTypeReference<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return get(path, mvm, responseType);
    }

    public <T, S> ResponseEntity<T> post(URI uri, @Nullable S body, Class<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.POST, new HttpEntity(body), responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.POST, new HttpEntity(body), responseType);
        }
    }

    public <T, S> ResponseEntity<T> post(String path, @Nullable S body, Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return post(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> post(String path, MultiValueMap<String, String> params,
                                        @Nullable S body, Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return post(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> post(String path, Map<String, String> params,
                                        @Nullable S body, Class<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return post(path, mvm, body, responseType);
    }

    public <T, S> ResponseEntity<T> post(URI uri, @Nullable S body,
                                         ParameterizedTypeReference<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.POST, new HttpEntity(body), responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.POST, new HttpEntity(body), responseType);
        }
    }

    public <T, S> ResponseEntity<T> post(String path, @Nullable S body,
                                         ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return post(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> post(String path, MultiValueMap<String, String> params,
                                         @Nullable S body,
                                         ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return post(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> post(String path, Map<String, String> params,
                                         @Nullable S body,
                                         ParameterizedTypeReference<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return post(path, mvm, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(URI uri, @Nullable S body, Class<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.PUT, new HttpEntity(body), responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.PUT, new HttpEntity(body), responseType);
        }
    }

    public <T, S> ResponseEntity<T> put(String path, @Nullable S body, Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return put(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(String path, MultiValueMap<String, String> params,
                                         @Nullable S body, Class<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return put(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(String path, Map<String, String> params,
                                         @Nullable S body, Class<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return put(path, mvm, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(URI uri, @Nullable S body,
                                        ParameterizedTypeReference<T> responseType) {
        var u = restTemplate.getUriTemplateHandler().expand(uri.toString());
        var cbName = getCircuitBreakerName(u);
        if (cbName.isPresent()) {
            return cbFactory.create(cbName.get()).run(() -> restTemplate
                    .exchange(u, HttpMethod.PUT, new HttpEntity(body), responseType));
        } else {
            return restTemplate.exchange(u, HttpMethod.PUT, new HttpEntity(body), responseType);
        }
    }

    public <T, S> ResponseEntity<T> put(String path, @Nullable S body,
                                        ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return put(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(String path, MultiValueMap<String, String> params,
                                        @Nullable S body,
                                        ParameterizedTypeReference<T> responseType) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return put(uri, body, responseType);
    }

    public <T, S> ResponseEntity<T> put(String path, Map<String, String> params,
                                        @Nullable S body,
                                        ParameterizedTypeReference<T> responseType) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return put(path, mvm, body, responseType);
    }
}
