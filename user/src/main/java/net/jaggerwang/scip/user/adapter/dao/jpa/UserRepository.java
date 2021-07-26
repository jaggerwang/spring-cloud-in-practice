package net.jaggerwang.scip.user.adapter.dao.jpa;

import net.jaggerwang.scip.user.adapter.dao.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jagger Wang
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
        Optional<User> findByUsername(String username);

        Optional<User> findByMobile(String mobile);

        Optional<User> findByEmail(String email);
}
