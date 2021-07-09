package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.UserService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Jagger Wang
 */
public class UserServiceImpl extends InternalService implements UserService {
    public UserServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
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
    public UserDTO infoByUsername(String username) {
        var params = new HashMap<String, String>();
        params.put("username", username);
        var response = getData("/user/infoByUsername", params);
        return objectMapper.convertValue(response.get("user"), UserDTO.class);
    }

    @Override
    public UserDTO infoByMobile(String mobile) {
        var params = new HashMap<String, String>();
        params.put("mobile", mobile);
        var response = getData("/user/infoByMobile", params);
        return objectMapper.convertValue(response.get("user"), UserDTO.class);
    }

    @Override
    public UserDTO infoByEmail(String email) {
        var params = new HashMap<String, String>();
        params.put("email", email);
        var response = getData("/user/infoByEmail", params);
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
}
