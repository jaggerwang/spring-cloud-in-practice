package net.jaggerwang.scip.stat.adapter.dao;

import java.util.Optional;

import net.jaggerwang.scip.stat.adapter.dao.jpa.entity.UserStat;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.stat.adapter.dao.jpa.UserStatRepository;
import net.jaggerwang.scip.common.entity.UserStatBO;
import net.jaggerwang.scip.stat.usecase.port.dao.UserStatDAO;

/**
 * @author Jagger Wang
 */
@Component
public class UserStatDAOImpl implements UserStatDAO {
    private UserStatRepository userStatRepository;

    public UserStatDAOImpl(UserStatRepository userStatRepository) {
        this.userStatRepository = userStatRepository;
    }

    @Override
    public UserStatBO save(UserStatBO userStatBO) {
        return userStatRepository.save(UserStat.fromBO(userStatBO)).toBO();
    }

    @Override
    public Optional<UserStatBO> findById(Long id) {
        return userStatRepository.findById(id).map(userStat -> userStat.toBO());
    }

    @Override
    public Optional<UserStatBO> findByUserId(Long userId) {
        return userStatRepository.findByUserId(userId).map(userStat -> userStat.toBO());
    }
}
