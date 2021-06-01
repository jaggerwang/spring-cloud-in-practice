package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserAsyncService {
    Mono<UserDTO> register(UserDTO userDto);

    Mono<UserDTO> verifyPassword(UserDTO userDto);

    Mono<UserDTO> modify(UserDTO userDto);

    Mono<UserDTO> modify(UserDTO userDto, String code);

    Mono<UserDTO> info(Long id);

    Mono<UserDTO> infoByUsername(String username, Boolean withPassword);

    Mono<UserDTO> infoByMobile(String mobile, Boolean withPassword);

    Mono<UserDTO> infoByEmail(String email, Boolean withPassword);

    Mono<List<RoleDTO>> roles(String username);

    Mono<Void> follow(Long userId);

    Mono<Void> unfollow(Long userId);

    Mono<Boolean> isFollowing(Long userId);

    Mono<List<UserDTO>> following(Long userId, Long limit, Long offset);

    Mono<Long> followingCount(Long userId);

    Mono<List<UserDTO>> follower(Long userId, Long limit, Long offset);

    Mono<Long> followerCount(Long userId);

    Mono<String> sendMobileVerifyCode(String type, String mobile);

    Mono<String> sendEmailVerifyCode(String type, String email);
}
