package net.jaggerwang.scip.post.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.post.entity.PostEntity;
import net.jaggerwang.scip.post.usecase.port.repository.PostRepository;
import net.jaggerwang.scip.post.usecase.port.service.UserService;

public class PostUsecases {
    private PostRepository postRepository;

    private UserService userService;

    public PostUsecases(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public PostEntity publish(PostEntity postEntity) {
        var post = PostEntity.builder().userId(postEntity.getUserId()).type(postEntity.getType())
                .text(postEntity.getText()).imageIds(postEntity.getImageIds())
                .videoId(postEntity.getVideoId()).build();
        return postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }

    public Optional<PostEntity> info(Long id) {
        return postRepository.findById(id);
    }

    public List<PostEntity> published(Long userId, Long limit, Long offset) {
        return postRepository.published(userId, limit, offset);
    }

    public Long publishedCount(Long userId) {
        return postRepository.publishedCount(userId);
    }

    public void like(Long userId, Long postId) {
        postRepository.like(userId, postId);
    }

    public void unlike(Long userId, Long postId) {
        postRepository.unlike(userId, postId);
    }

    public Boolean isLiked(Long userId, Long postId) {
        return postRepository.isLiked(userId, postId);
    }

    public List<PostEntity> liked(Long userId, Long limit, Long offset) {
        return postRepository.liked(userId, limit, offset);
    }

    public Long likedCount(Long userId) {
        return postRepository.likedCount(userId);
    }

    public List<PostEntity> following(Long userId, Long limit, Long beforeId, Long afterId) {
        var userIds = userService.following(userId, null, null).stream()
                .map(userDto -> userDto.getId()).collect(Collectors.toList());

        return postRepository.following(userIds, limit, beforeId, afterId);
    }

    public Long followingCount(Long userId) {
        var userIds = userService.following(userId, null, null).stream()
                .map(userDto -> userDto.getId()).collect(Collectors.toList());

        return postRepository.followingCount(userIds);
    }
}
