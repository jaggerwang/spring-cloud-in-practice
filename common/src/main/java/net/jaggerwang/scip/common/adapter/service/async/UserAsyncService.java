package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.async.UserService;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserAsyncService extends InternalAsyncService implements UserService {
    protected ObjectMapper objectMapper;

    public UserAsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                            ObjectMapper objectMapper) {
        super(webClient, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<UserDto> register(UserDto userDto) {
        return postData("/user/register", userDto)
                .map(data -> objectMapper.convertValue(data.get("post"), UserDto.class));
    }

    @Override
    public Mono<UserDto> verifyPasswordByUsername(String username, String password) {
        return getData("/user/verifyPassword", Map.of("username", username, "password", password))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> verifyPasswordByMobile(String mobile, String password) {
        return getData("/user/verifyPassword", Map.of("mobile", mobile, "password", password))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> verifyPasswordByEmail(String email, String password) {
        return getData("/user/verifyPassword", Map.of("email", email, "password", password))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> logged() {
        return getData("/user/logged")
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<UserDto> modify(UserDto userDto) {
        return postData("/user/modify", userDto)
                .map(data -> objectMapper.convertValue(data.get("post"), UserDto.class));
    }

    @Override
    public Mono<UserDto> modify(UserDto userDto, String code) {
        var params = objectMapper.convertValue(userDto, new TypeReference<Map<String, String>>() {});
        params.put("code", code);
        return postData("/user/modify", params)
                .map(data -> objectMapper.convertValue(data.get("post"), UserDto.class));
    }

    @Override
    public Mono<UserDto> info(Long id) {
        return getData("/user/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("user"), UserDto.class));
    }

    @Override
    public Mono<Void> follow(Long userId) {
        return postData("/user/follow", Map.of("userId", userId.toString()))
                .map(data -> null);
    }

    @Override
    public Mono<Void> unfollow(Long userId) {
        return postData("/user/unfollow", Map.of("userId", userId.toString()))
                .map(data -> null);
    }

    @Override
    public Mono<Boolean> isFollowing(Long userId) {
        return getData("/user/isFollowing", Map.of("userId", userId.toString()))
                .map(data -> objectMapper.convertValue(data.get("isFollowing"), Boolean.class));
    }

    @Override
    public Mono<List<UserDto>> following(Long userId, Long limit, Long offset) {
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
    public Mono<Long> followingCount(Long userId) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        return getData("/user/followingCount", params)
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }

    @Override
    public Mono<List<UserDto>> follower(Long userId, Long limit, Long offset) {
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
    public Mono<Long> followerCount(Long userId) {
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
                .map(data -> objectMapper.convertValue(data.get("count"), String.class));
    }

    @Override
    public Mono<String> sendEmailVerifyCode(String type, String email) {
        return postData("/user/sendEmailVerifyCode", Map.of("type", type, "email", email))
                .map(data -> objectMapper.convertValue(data.get("count"), String.class));
    }
}
