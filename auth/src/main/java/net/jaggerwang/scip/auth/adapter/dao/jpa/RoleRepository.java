package net.jaggerwang.scip.auth.adapter.dao.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.Role;

/**
 * @author Jagger Wang
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
        Optional<Role> findByName(String name);
}
