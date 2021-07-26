package net.jaggerwang.scip.user.adapter.dao.jpa;

import net.jaggerwang.scip.user.adapter.dao.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jagger Wang
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
        Optional<Role> findByName(String name);
}
