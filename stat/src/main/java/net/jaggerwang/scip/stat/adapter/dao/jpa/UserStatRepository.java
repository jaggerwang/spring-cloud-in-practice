package net.jaggerwang.scip.stat.adapter.dao.jpa;

import java.util.Optional;

import net.jaggerwang.scip.stat.adapter.dao.jpa.entity.UserStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatRepository
        extends JpaRepository<UserStat, Long>, QuerydslPredicateExecutor<UserStat> {
    Optional<UserStat> findByUserId(Long userId);
}
