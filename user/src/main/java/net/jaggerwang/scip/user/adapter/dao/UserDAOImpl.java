package net.jaggerwang.scip.user.adapter.dao;

import net.jaggerwang.scip.user.adapter.dao.jpa.UserBindRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.User;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserBind;
import net.jaggerwang.scip.user.usecase.port.dao.UserDAO;
import net.jaggerwang.scip.common.entity.UserBO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Jagger Wang
 */
@Component
public class UserDAOImpl implements UserDAO {
    private UserRepository userRepository;
    private UserBindRepository userBindRepository;

    public UserDAOImpl(UserRepository userRepository, UserBindRepository userBindRepository) {
        this.userRepository = userRepository;
        this.userBindRepository = userBindRepository;
    }

    @Override
    public UserBO save(UserBO userBO) {
        return userRepository.save(User.fromBO(userBO)).toBO();
    }

    @Override
    public Optional<UserBO> findById(Long id) {
        return userRepository.findById(id).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile).map(User::toBO);
    }

    @Override
    public UserBO bindInternalUser(String externalAuthProvider, String externalUserId,
                                   UserBO userBO) {
        var bindedUserBO = findInternalUser(externalAuthProvider, externalUserId);
        if (bindedUserBO.isPresent()) {
            return bindedUserBO.get();
        }

        if (userBO.getId() == null) {
            userBO = save(userBO);
        }

        userBindRepository.save(UserBind.builder()
                .externalAuthProvider(externalAuthProvider)
                .externalUserId(externalUserId)
                .internalUserId(userBO.getId())
                .build());

        return userBO;
    }

    @Override
    public Optional<UserBO> findInternalUser(String externalAuthProvider, String externalUserId) {
        var userBind = userBindRepository.findByExternalAuthProviderAndExternalUserId(
                externalAuthProvider, externalUserId);
        if (userBind == null) {
            return Optional.empty();
        }

        return findById(userBind.getInternalUserId());
    }
}
