package net.jaggerwang.scip.auth.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.RoleBO;

public interface RoleDAO {
    RoleBO save(RoleBO userEntity);

    Optional<RoleBO> findById(Long id);

    Optional<RoleBO> findByName(String name);

    List<RoleBO> rolesOfUser(Long userId);

    List<RoleBO> rolesOfUser(String username);
}
