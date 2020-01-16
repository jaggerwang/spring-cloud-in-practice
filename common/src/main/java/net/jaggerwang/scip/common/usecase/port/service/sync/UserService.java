package net.jaggerwang.scip.common.usecase.port.service.sync;

import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> following(Long userId, Long limit, Long offset);

    Long followingCount(Long userId);
}
