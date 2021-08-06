package net.jaggerwang.scip.user.usecase.port.dao;

import net.jaggerwang.scip.common.entity.UserBO;

import java.util.Optional;

/**
 * @author Jagger Wang
 */
public interface UserDAO {
    UserBO save(UserBO userBO);

    Optional<UserBO> findById(Long id);

    Optional<UserBO> findByUsername(String username);

    Optional<UserBO> findByMobile(String mobile);

    Optional<UserBO> findByEmail(String email);

    UserBO bindInternalUser(String externalAuthProvider, String externalUserId, UserBO userBO);

    Optional<UserBO> findInternalUser(String externalAuthProvider, String externalUserId);
}
