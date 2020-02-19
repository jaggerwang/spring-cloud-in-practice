package net.jaggerwang.scip.common.adapter.service.sync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.InternalApiException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

public abstract class InternalSyncService extends SyncService {
    protected ObjectMapper objectMapper;

    public InternalSyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);
        this.objectMapper = objectMapper;
    }

    private ResponseEntity<RootDto> fallback(Throwable throwable) {
        var status = HttpStatus.SERVICE_UNAVAILABLE;
        var body = new RootDto("fail", throwable.getMessage());
        if (throwable instanceof HttpStatusCodeException) {
            var sce = (HttpStatusCodeException) throwable;
            status = sce.getStatusCode();
            try {
                body = objectMapper.readValue(sce.getResponseBodyAsString(), RootDto.class);
            } catch (JsonProcessingException e) {
            }
        }
        return ResponseEntity.status(status).body(body);
    }

    public Map<String, Object> getData(URI uri) {
        var response = get(uri, RootDto.class, this::fallback);
        if (!Objects.equals(response.getBody().getCode(), "ok")) {
            throw new InternalApiException(response.getStatusCode(), response.getBody().getCode(),
                    response.getBody().getMessage(), response.getBody().getData());
        }
        return response.getBody().getData();
    }

    public Map<String, Object> getData(String path) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return getData(uri);
    }

    public Map<String, Object> getData(String path, MultiValueMap<String, String> params) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return getData(uri);
    }

    public Map<String, Object> getData(String path, Map<String, String> params) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return getData(path, mvm);
    }

    public <T> Map<String, Object> postData(URI uri, @Nullable T body) {
        var response = post(uri, body, RootDto.class, this::fallback);
        if (!Objects.equals(response.getBody().getCode(), "ok")) {
            throw new InternalApiException(response.getStatusCode(), response.getBody().getCode(),
                    response.getBody().getMessage(), response.getBody().getData());
        }
        return response.getBody().getData();
    }

    public <T> Map<String, Object> postData(String path, @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .build();
        return postData(uri, body);
    }

    public <T> Map<String, Object> postData(String path, MultiValueMap<String, String> params,
                                        @Nullable T body) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path(path)
                .queryParams(params)
                .build();
        return postData(uri, body);
    }

    public <T> Map<String, Object> postData(String path, Map<String, String> params,
                                        @Nullable T body) {
        var mvm = new LinkedMultiValueMap<String, String>();
        mvm.setAll(params);
        return postData(path, mvm, body);
    }
}
