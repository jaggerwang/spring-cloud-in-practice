package net.jaggerwang.scip.user.adapter.repository.jpa;

import net.jaggerwang.scip.user.adapter.repository.jpa.entity.UserRoleDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleDo, Long>, QuerydslPredicateExecutor<UserRoleDo> {
}
