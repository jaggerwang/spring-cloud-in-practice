package net.jaggerwang.scip.user.adapter.dao;

import net.jaggerwang.scip.user.adapter.dao.jpa.UserRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.User;
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

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
