package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class QueryResolver extends AbstractResolver implements GraphQLQueryResolver {
    @PermitAll
    public Mono<UserDto> userLogged() {
        if (loggedUserId() == null) {
            return null;
        }

        return userService.info(loggedUserId());
    }

    public Mono<UserDto> userInfo(Long id) {
        return userService.info(id);
    }

    public Mono<List<UserDto>> userFollowing(Long userId, Long limit, Long offset) {
        return userService.following(userId, limit, offset);
    }

    public Mono<Long> userFollowingCount(Long userId) {
        return userService.followingCount(userId);
    }

    public Mono<List<UserDto>> userFollower(Long userId, Long limit, Long offset) {
        if (limit == null) {
            limit = 20L;
        }

        return userService.follower(userId, limit, offset);
    }

    public Mono<Long> userFollowerCount(Long userId) {
        return userService.followerCount(userId);
    }

    public Mono<PostDto> postInfo(Long id) {
        return postService.info(id);
    }

    public Mono<List<PostDto>> postPublished(Long userId, Long limit, Long offset) {
        return postService.published(userId, limit, offset);
    }

    public Mono<Long> postPublishedCount(Long userId) {
        return postService.publishedCount(userId);
    }

    public Mono<List<PostDto>> postLiked(Long userId, Long limit, Long offset) {
        return postService.liked(userId, limit, offset);
    }

    public Mono<Long> postLikedCount(Long userId) {
        return postService.likedCount(userId);
    }

    public Mono<List<PostDto>> postFollowing(Long limit, Long beforeId, Long afterId) {
        return postService.following(limit, beforeId, afterId);
    }

    public Mono<Long> postFollowingCount() {
        return postService.followingCount();
    }

    public Mono<FileDto> fileInfo(Long id) {
        return fileService.info(id);
    }
}
