package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;

import java.util.List;

public interface UserSyncService {
    UserDTO info(Long id);

    List<UserDTO> following(Long userId, Long limit, Long offset);

    Long followingCount(Long userId);

    Boolean isFollowing(Long userId);
}
