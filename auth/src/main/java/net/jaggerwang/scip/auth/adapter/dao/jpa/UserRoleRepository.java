package net.jaggerwang.scip.auth.adapter.dao.jpa;

import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, QuerydslPredicateExecutor<UserRole> {
}
