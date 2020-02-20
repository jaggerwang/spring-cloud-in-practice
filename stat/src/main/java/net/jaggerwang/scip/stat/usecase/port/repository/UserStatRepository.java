package net.jaggerwang.scip.stat.usecase.port.repository;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserStatEntity;

public interface UserStatRepository {
    UserStatEntity save(UserStatEntity userStatEntity);

    Optional<UserStatEntity> findById(Long id);

    Optional<UserStatEntity> findByUserId(Long userId);
}
