package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDTO;
import net.jaggerwang.scip.common.usecase.port.service.PostSyncService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

public class PostSyncServiceImpl extends InternalSyncService implements PostSyncService {
    public PostSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public PostDTO info(Long id) {
        var params = new HashMap<String, String>();
        params.put("id", id.toString());
        var response = getData("/post/info", params);
        return objectMapper.convertValue(response.get("post"), PostDTO.class);
    }
}
