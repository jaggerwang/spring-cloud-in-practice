package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.exception.InternalApiException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
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

/**
 * @author Jagger Wang
 */
public abstract class InternalService extends Service {
    protected ObjectMapper objectMapper;

    public InternalService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);
        this.objectMapper = objectMapper;
    }

    private ResponseEntity<RootDTO> fallback(Throwable throwable) {
        var status = HttpStatus.SERVICE_UNAVAILABLE;
        var body = new RootDTO("fail", throwable.toString());
        if (throwable instanceof HttpStatusCodeException) {
            var sce = (HttpStatusCodeException) throwable;
            status = sce.getStatusCode();
            try {
                body = objectMapper.readValue(sce.getResponseBodyAsString(), RootDTO.class);
            } catch (JsonProcessingException e) {
            }
        } else {
            throwable.printStackTrace();
        }
        return ResponseEntity.status(status).body(body);
    }

    private Map<String, Object> handleResponse(ResponseEntity<RootDTO> response) {
        if (!Objects.equals(response.getBody().getCode(), "ok")) {
            throw new InternalApiException(response.getStatusCode(), response.getBody().getCode(),
                    response.getBody().getMessage(), response.getBody().getData());
        }
        return response.getBody().getData();
    }

    public Map<String, Object> getData(URI uri) {
        var response = get(uri, null, RootDTO.class, this::fallback);
        return handleResponse(response);
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
        var requestEntity = new HttpEntity<>(body);
        var response = post(uri, requestEntity, RootDTO.class, this::fallback);
        return handleResponse(response);
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
