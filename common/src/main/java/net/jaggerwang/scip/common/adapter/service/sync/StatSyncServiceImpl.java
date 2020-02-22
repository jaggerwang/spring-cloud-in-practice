package net.jaggerwang.scip.common.adapter.service.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;
import net.jaggerwang.scip.common.usecase.port.service.sync.StatSyncService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

public class StatSyncServiceImpl extends InternalSyncService implements StatSyncService {
    public StatSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    public StatSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper, HttpServletRequest upstreamRequest) {
        super(restTemplate, cbFactory, objectMapper, upstreamRequest);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public UserStatDto ofUser(Long userId) {
        var params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        var response = getData("/stat/ofUser", params);
        return objectMapper.convertValue(response.get("userStat"), UserStatDto.class);
    }

    @Override
    public PostStatDto ofPost(Long postId) {
        var params = new HashMap<String, String>();
        params.put("postId", postId.toString());
        var response = getData("/stat/ofPost", params);
        return objectMapper.convertValue(response.get("postStat"), PostStatDto.class);
    }
}
