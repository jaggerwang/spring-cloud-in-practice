package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserAsyncServiceImpl extends InternalAsyncService implements UserAsyncService {
    public UserAsyncServiceImpl(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                                ObjectMapper objectMapper) {
        super(webClient, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<UserDTO> register(UserDTO userDto) {
        return postData("/user/register", userDto)
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> verifyPassword(UserDTO userDto) {
        var params = new HashMap<String, String>();
        if (userDto.getUsername() != null) params.put("username", userDto.getUsername());
        if (userDto.getMobile() != null) params.put("mobile", userDto.getMobile());
        if (userDto.getEmail() != null) params.put("email", userDto.getEmail());
        params.put("password", userDto.getPassword());

        return getData("/user/verifyPassword", params)
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> modify(UserDTO userDto) {
        return postData("/user/modify", Map.of("user", userDto))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> modify(UserDTO userDto, String code) {
        return postData("/user/modify", Map.of("user", userDto, "code", code))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> info(Long id) {
        return getData("/user/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> infoByUsername(String username, Boolean withPassword) {
        return getData("/user/infoByUsername", Map.of("username", username,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> infoByMobile(String mobile, Boolean withPassword) {
        return getData("/user/infoByMobile", Map.of("mobile", mobile,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<UserDTO> infoByEmail(String email, Boolean withPassword) {
        return getData("/user/infoByEmail", Map.of("email", email,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDTO.class));
    }

    @Override
    public Mono<List<RoleDTO>> roles(String username) {
        return getData("/user/roles", Map.of("username", username))
                .map(data -> objectMapper.convertValue(data.get("roles"), new TypeReference<>() {}));
    }

    @Override
    public Mono<Void> follow(Long userId) {
        return postData("/user/follow", Map.of("userId", userId.toString()))
                .then();
    }

    @Override
    public Mono<Void> unfollow(Long userId) {
        return postData("/user/unfollow", Map.of("userId", userId.toString()))
                .then();
    }

    @Override
    public Mono<Boolean> isFollowing(Long userId) {
        return getData("/user/isFollowing", Map.of("userId", userId.toString()))
                .map(data -> objectMapper.convertValue(data.get("isFollowing"), Boolean.class));
    }

    @Override
    public Mono<List<UserDTO>> following(@Nullable Long userId, @Nullable Long limit,
                                         @Nullable Long offset) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (offset != null) {
            params.put("offset", offset.toString());
        }
        return getData("/user/following", params)
                .map(data -> objectMapper.convertValue(data.get("users"), new TypeReference<>(){}));
    }

    @Override
    public Mono<Long> followingCount(@Nullable Long userId) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        return getData("/user/followingCount", params)
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }

    @Override
    public Mono<List<UserDTO>> follower(@Nullable Long userId, @Nullable Long limit,
                                        @Nullable Long offset) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (offset != null) {
            params.put("offset", offset.toString());
        }
        return getData("/user/follower", params)
                .map(data -> objectMapper.convertValue(data.get("users"), new TypeReference<>(){}));
    }

    @Override
    public Mono<Long> followerCount(@Nullable Long userId) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        return getData("/user/followerCount", params)
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }

    @Override
    public Mono<String> sendMobileVerifyCode(String type, String mobile) {
        return postData("/user/sendMobileVerifyCode", Map.of("type", type, "mobile", mobile))
                .map(data -> objectMapper.convertValue(data.get("verifyCode"), String.class));
    }

    @Override
    public Mono<String> sendEmailVerifyCode(String type, String email) {
        return postData("/user/sendEmailVerifyCode", Map.of("type", type, "email", email))
                .map(data -> objectMapper.convertValue(data.get("verifyCode"), String.class));
    }
}
