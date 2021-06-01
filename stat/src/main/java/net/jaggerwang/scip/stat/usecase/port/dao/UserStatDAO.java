package net.jaggerwang.scip.stat.usecase.port.dao;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserStatBO;

public interface UserStatDAO {
    UserStatBO save(UserStatBO userStatBO);

    Optional<UserStatBO> findById(Long id);

    Optional<UserStatBO> findByUserId(Long userId);
}
