package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;

import java.util.List;

/**
 * @author Jagger Wang
 */
public interface UserService {
    UserDTO info(Long id);

    UserDTO infoByUsername(String username);

    UserDTO infoByMobile(String mobile);

    UserDTO infoByEmail(String email);

    List<UserDTO> following(Long userId, Long limit, Long offset);

    Long followingCount(Long userId);
}
