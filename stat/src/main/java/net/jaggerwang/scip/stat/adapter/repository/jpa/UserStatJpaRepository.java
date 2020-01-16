package net.jaggerwang.scip.stat.adapter.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import net.jaggerwang.scip.stat.adapter.repository.jpa.entity.UserStatDo;

@Repository
public interface UserStatJpaRepository
        extends JpaRepository<UserStatDo, Long>, QuerydslPredicateExecutor<UserStatDo> {
    Optional<UserStatDo> findByUserId(Long userId);
}
