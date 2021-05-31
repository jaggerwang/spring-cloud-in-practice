package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserAsyncService {
    Mono<UserDto> register(UserDto userDto);

    Mono<UserDto> verifyPassword(UserDto userDto);

    Mono<UserDto> modify(UserDto userDto);

    Mono<UserDto> modify(UserDto userDto, String code);

    Mono<UserDto> info(Long id);

    Mono<UserDto> infoByUsername(String username, Boolean withPassword);

    Mono<UserDto> infoByMobile(String mobile, Boolean withPassword);

    Mono<UserDto> infoByEmail(String email, Boolean withPassword);

    Mono<List<RoleDto>> roles(String username);

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
