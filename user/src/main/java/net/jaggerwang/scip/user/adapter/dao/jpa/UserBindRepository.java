package net.jaggerwang.scip.user.adapter.dao.jpa;

import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Jagger Wang
 */
@Repository
public interface UserBindRepository extends JpaRepository<UserBind, Long>, QuerydslPredicateExecutor<UserBind> {
    UserBind findByExternalAuthProviderAndExternalUserId(String externalAuthProvider, String externalUserId);
}
