package net.jaggerwang.scip.stat.usecase.port.dao;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserStatEntity;

public interface UserStatDAO {
    UserStatEntity save(UserStatEntity userStatEntity);

    Optional<UserStatEntity> findById(Long id);

    Optional<UserStatEntity> findByUserId(Long userId);
}
