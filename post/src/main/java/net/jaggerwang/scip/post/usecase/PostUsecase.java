package net.jaggerwang.scip.post.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.entity.PostBO;
import net.jaggerwang.scip.common.usecase.port.service.UserService;
import net.jaggerwang.scip.post.usecase.port.dao.PostDAO;
import org.springframework.stereotype.Component;

/**
 * @author Jagger Wang
 */
@Component
public class PostUsecase {
    private PostDAO postDAO;
    private UserService userService;

    public PostUsecase(PostDAO postDAO, UserService userService) {
        this.postDAO = postDAO;
        this.userService = userService;
    }

    public PostBO publish(PostBO postBO) {
        var post = PostBO.builder().userId(postBO.getUserId()).type(postBO.getType())
                .text(postBO.getText()).imageIds(postBO.getImageIds())
                .videoId(postBO.getVideoId()).build();
        return postDAO.save(post);
    }

    public void delete(Long id) {
        postDAO.delete(id);
    }

    public Optional<PostBO> info(Long id) {
        return postDAO.findById(id);
    }

    public List<PostBO> published(Long userId, Long limit, Long offset) {
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

    public List<PostBO> liked(Long userId, Long limit, Long offset) {
        return postDAO.liked(userId, limit, offset);
    }

    public Long likedCount(Long userId) {
        return postDAO.likedCount(userId);
    }

    public List<PostBO> following(Long userId, Long limit, Long beforeId, Long afterId) {
        var userIds = userService.following(userId, null, null).getData();

        return postDAO.following(userIds, limit, beforeId, afterId);
    }

    public Long followingCount(Long userId) {
        return userService.followingCount(userId).getData();
    }
}
