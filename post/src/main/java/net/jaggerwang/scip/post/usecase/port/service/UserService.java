package net.jaggerwang.scip.post.usecase.port.service;

import net.jaggerwang.scip.post.usecase.port.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> following(Long userId, Long limit, Long offset);

    Long followingCount(Long userId);
}
