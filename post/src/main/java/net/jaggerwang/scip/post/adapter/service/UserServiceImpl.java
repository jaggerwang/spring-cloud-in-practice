package net.jaggerwang.scip.post.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.SyncInternalService;
import net.jaggerwang.scip.post.usecase.port.service.UserService;
import net.jaggerwang.scip.post.usecase.port.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl extends SyncInternalService implements UserService {
    protected ObjectMapper objectMapper;

    public UserServiceImpl(@Qualifier("userServiceRestTemplate") RestTemplate restTemplate,
                           CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public List<UserDto> following(Long userId, Long limit, Long offset) {
        var params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (offset != null) {
            params.put("offset", offset.toString());
        }
        var response = getData("/user/following", params);
        return objectMapper.convertValue(response.get("users"), new TypeReference<>() {});
    }

    @Override
    public Long followingCount(Long userId) {
        var params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        var response = getData("/user/followingCount", params);
        return objectMapper.convertValue(response.get("count"), Long.class);
    }
}
