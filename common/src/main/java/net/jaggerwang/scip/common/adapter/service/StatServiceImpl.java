package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.StatService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

public class StatServiceImpl extends InternalService implements StatService {
    public StatServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public UserStatDTO ofUser(Long userId) {
        var params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        var response = getData("/stat/ofUser", params);
        return objectMapper.convertValue(response.get("userStat"), UserStatDTO.class);
    }

    @Override
    public PostStatDTO ofPost(Long postId) {
        var params = new HashMap<String, String>();
        params.put("postId", postId.toString());
        var response = getData("/stat/ofPost", params);
        return objectMapper.convertValue(response.get("postStat"), PostStatDTO.class);
    }
}
