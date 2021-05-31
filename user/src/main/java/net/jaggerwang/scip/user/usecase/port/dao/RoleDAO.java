package net.jaggerwang.scip.user.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.RoleEntity;

public interface RoleDAO {
    RoleEntity save(RoleEntity userEntity);

    Optional<RoleEntity> findById(Long id);

    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> rolesOfUser(Long userId);

    List<RoleEntity> rolesOfUser(String username);
}
