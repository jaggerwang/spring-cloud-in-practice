package net.jaggerwang.scip.post.adapter.dao.jpa;

import net.jaggerwang.scip.post.adapter.dao.jpa.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository
                extends JpaRepository<PostLike, Long>, QuerydslPredicateExecutor<PostLike> {
        @Transactional
        void deleteByUserIdAndPostId(Long userId, Long postId);

        Boolean existsByUserIdAndPostId(Long userId, Long postId);
}
