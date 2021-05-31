package net.jaggerwang.scip.post.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.UserSyncService;
import net.jaggerwang.scip.common.entity.PostEntity;
import net.jaggerwang.scip.post.usecase.port.dao.PostDAO;

public class PostUsecase {
    private PostDAO postDAO;
    private UserSyncService userSyncService;

    public PostUsecase(PostDAO postDAO, UserSyncService userSyncService) {
        this.postDAO = postDAO;
        this.userSyncService = userSyncService;
    }

    public PostEntity publish(PostEntity postEntity) {
        var post = PostEntity.builder().userId(postEntity.getUserId()).type(postEntity.getType())
                .text(postEntity.getText()).imageIds(postEntity.getImageIds())
                .videoId(postEntity.getVideoId()).build();
        return postDAO.save(post);
    }

    public void delete(Long id) {
        postDAO.delete(id);
    }

    public Optional<PostEntity> info(Long id) {
        return postDAO.findById(id);
    }

    public List<PostEntity> published(Long userId, Long limit, Long offset) {
        return postDAO.published(userId, limit, offset);
    }

    public Long publishedCount(Long userId) {
        return postDAO.publishedCount(userId);
    }

    public void like(Long userId, Long postId) {
        postDAO.like(userId, postId);
    }

    public void unlike(Long userId, Long postId) {
        postDAO.unlike(userId, postId);
    }

    public Boolean isLiked(Long userId, Long postId) {
        return postDAO.isLiked(userId, postId);
    }

    public List<PostEntity> liked(Long userId, Long limit, Long offset) {
        return postDAO.liked(userId, limit, offset);
    }

    public Long likedCount(Long userId) {
        return postDAO.likedCount(userId);
    }

    public List<PostEntity> following(Long userId, Long limit, Long beforeId, Long afterId) {
        var userIds = userSyncService.following(userId, null, null).stream()
                .map(userDto -> userDto.getId()).collect(Collectors.toList());

        return postDAO.following(userIds, limit, beforeId, afterId);
    }

    public Long followingCount(Long userId) {
        var userIds = userSyncService.following(userId, null, null).stream()
                .map(userDto -> userDto.getId()).collect(Collectors.toList());

        return postDAO.followingCount(userIds);
    }
}
