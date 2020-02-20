package net.jaggerwang.scip.user.usecase.port.repository;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.RoleEntity;

public interface RoleRepository {
    RoleEntity save(RoleEntity userEntity);

    Optional<RoleEntity> findById(Long id);

    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> rolesOfUser(Long userId);

    List<RoleEntity> rolesOfUser(String username);
}
