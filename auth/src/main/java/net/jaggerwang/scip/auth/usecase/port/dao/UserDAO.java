package net.jaggerwang.scip.auth.usecase.port.dao;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserBO;

public interface UserDAO {
    UserBO save(UserBO userBO);

    Optional<UserBO> findById(Long id);

    Optional<UserBO> findByUsername(String username);

    Optional<UserBO> findByMobile(String mobile);

    Optional<UserBO> findByEmail(String email);
}
