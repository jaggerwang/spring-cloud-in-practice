package net.jaggerwang.scip.post.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.PostEntity;

public interface PostDAO {
    PostEntity save(PostEntity postEntity);

    void delete(Long id);

    Optional<PostEntity> findById(Long id);

    List<PostEntity> published(Long userId, Long limit, Long offset);

    Long publishedCount(Long userId);

    void like(Long userId, Long postId);

    void unlike(Long userId, Long postId);

    List<PostEntity> liked(Long userId, Long limit, Long offset);

    Long likedCount(Long userId);

    Boolean isLiked(Long userId, Long postId);

    List<PostEntity> following(List<Long> userIds, Long limit, Long beforeId, Long afterId);

    Long followingCount(List<Long> userIds);
}
