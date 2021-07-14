package net.jaggerwang.scip.user.adapter.dao.jpa;

import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserFollow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Repository
public interface UserFollowRepository
                extends JpaRepository<UserFollow, Long>, QuerydslPredicateExecutor<UserFollow> {
        @Transactional
        void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

        Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

        List<UserFollow> findAllByFollowerId(Long followerId, Pageable pageable);

        Long countByFollowerId(Long followerId);

        List<UserFollow> findAllByFollowingId(Long followingId, Pageable pageable);

        Long countByFollowingId(Long followingId);
}
