package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.UserSyncService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UserSyncServiceImpl extends InternalSyncService implements UserSyncService {
    public UserSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public UserDTO info(Long id) {
        var params = new HashMap<String, String>();
        params.put("id", id.toString());
        var response = getData("/user/info", params);
        return objectMapper.convertValue(response.get("user"), UserDTO.class);
    }

    @Override
    public List<UserDTO> following(Long userId, @Nullable Long limit, @Nullable Long offset) {
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

    @Override
    public Boolean isFollowing(Long userId) {
        var params = new HashMap<String, String>();
        params.put("userId", userId.toString());
        var response = getData("/user/isFollowing", params);
        return objectMapper.convertValue(response.get("isFollowing"), Boolean.class);
    }
}
