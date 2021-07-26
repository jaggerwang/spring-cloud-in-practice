package net.jaggerwang.scip.user.usecase.port.dao;

import net.jaggerwang.scip.common.entity.RoleBO;

import java.util.List;
import java.util.Optional;

/**
 * @author Jagger Wang
 */
public interface RoleDAO {
    RoleBO save(RoleBO userEntity);

    Optional<RoleBO> findById(Long id);

    Optional<RoleBO> findByName(String name);

    List<RoleBO> rolesOfUser(Long userId);
}
