package net.jaggerwang.scip.post.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.PostBO;

public interface PostDAO {
    PostBO save(PostBO postBO);

    void delete(Long id);

    Optional<PostBO> findById(Long id);

    List<PostBO> published(Long userId, Long limit, Long offset);

    Long publishedCount(Long userId);

    void like(Long userId, Long postId);

    void unlike(Long userId, Long postId);

    List<PostBO> liked(Long userId, Long limit, Long offset);

    Long likedCount(Long userId);

    Boolean isLiked(Long userId, Long postId);

    List<PostBO> following(List<Long> userIds, Long limit, Long beforeId, Long afterId);

    Long followingCount(List<Long> userIds);
}
