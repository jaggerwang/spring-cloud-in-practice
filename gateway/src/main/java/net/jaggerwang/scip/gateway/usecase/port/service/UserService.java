package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Mono<UserDto> register(UserDto userDto);

    Mono<UserDto> verifyPasswordByUsername(String username, String password);

    Mono<UserDto> verifyPasswordByMobile(String mobile, String password);

    Mono<UserDto> verifyPasswordByEmail(String email, String password);

    Mono<UserDto> logged();

    Mono<UserDto> modify(UserDto userDto);

    Mono<UserDto> modify(UserDto userDto, String code);

    Mono<UserDto> info(Long id);

    Mono<Void> follow(Long userId);

    Mono<Void> unfollow(Long userId);

    Mono<Boolean> isFollowing(Long userId);

    Mono<List<UserDto>> following(Long userId, Long limit, Long offset);

    Mono<Long> followingCount(Long userId);

    Mono<List<UserDto>> follower(Long userId, Long limit, Long offset);

    Mono<Long> followerCount(Long userId);

    Mono<String> sendMobileVerifyCode(String type, String mobile);

    Mono<String> sendEmailVerifyCode(String type, String email);
}
