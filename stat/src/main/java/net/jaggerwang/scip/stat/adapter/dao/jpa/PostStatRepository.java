package net.jaggerwang.scip.stat.adapter.dao.jpa;

import java.util.Optional;

import net.jaggerwang.scip.stat.adapter.dao.jpa.entity.PostStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatRepository
        extends JpaRepository<PostStat, Long>, QuerydslPredicateExecutor<PostStat> {
    Optional<PostStat> findByPostId(Long postId);
}
