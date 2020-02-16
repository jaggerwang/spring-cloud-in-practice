package net.jaggerwang.scip.common.adapter.service.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.sync.PostService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

public class PostSyncService extends InternalSyncService implements PostService {
    protected ObjectMapper objectMapper;

    public PostSyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public PostDto info(Long id) {
        var params = new HashMap<String, String>();
        params.put("id", id.toString());
        var response = getData("/post/info", params);
        return objectMapper.convertValue(response.get("post"), PostDto.class);
    }
}
