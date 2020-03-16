package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
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
    public Mono<UserDto> register(UserDto userDto) {
        return postData("/user/register", userDto)
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> verifyPassword(UserDto userDto) {
        return getData("/user/verifyPassword", Map.of("username", userDto.getUsername(),
                "mobile", userDto.getMobile(), "email", userDto.getEmail(),
                "password", userDto.getPassword()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> modify(UserDto userDto) {
        return postData("/user/modify", Map.of("user", userDto))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> modify(UserDto userDto, String code) {
        return postData("/user/modify", Map.of("user", userDto, "code", code))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> info(Long id) {
        return getData("/user/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> infoByUsername(String username, Boolean withPassword) {
        return getData("/user/infoByUsername", Map.of("username", username,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> infoByMobile(String mobile, Boolean withPassword) {
        return getData("/user/infoByMobile", Map.of("mobile", mobile,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> infoByEmail(String email, Boolean withPassword) {
        return getData("/user/infoByEmail", Map.of("email", email,
                "withPassword", withPassword.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<List<RoleDto>> roles(String username) {
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
    public Mono<List<UserDto>> following(@Nullable Long userId, @Nullable Long limit,
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
    public Mono<List<UserDto>> follower(@Nullable Long userId, @Nullable Long limit,
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
