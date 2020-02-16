package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryDataFetchers extends AbstractDataFetchers {
    public DataFetcher userLogged() {
        return env -> {
            if (loggedUserId() == null) {
                return null;
            }

            return userService.info(loggedUserId());
        };
    }

    public DataFetcher userInfo() {
        return env -> {
            Long id = env.getArgument("id");
            return userService.info(id);
        };
    }

    public DataFetcher userFollowing() {
        return env -> {
            Long userId = env.getArgument("userId");
            Long limit = env.getArgument("limit");
            Long offset = env.getArgument("offset");
            return userService.following(userId, limit, offset);
        };
    }

    public DataFetcher userFollowingCount() {
        return env -> {
            Long userId = env.getArgument("userId");
            return userService.followingCount(userId);
        };
    }

    public DataFetcher userFollower() {
        return env -> {
            Long userId = env.getArgument("userId");
            Long limit = env.getArgument("limit");
            Long offset = env.getArgument("offset");
            return userService.follower(userId, limit, offset);
        };
    }

    public DataFetcher userFollowerCount() {
        return env -> {
            Long userId = env.getArgument("userId");
            return userService.followerCount(userId);
        };
    }

    public DataFetcher postInfo() {
        return env -> {
            Long id = env.getArgument("id");
            return postService.info(id);
        };
    }

    public DataFetcher postPublished() {
        return env -> {
            Long userId = env.getArgument("userId");
            Long limit = env.getArgument("limit");
            Long offset = env.getArgument("offset");
            return postService.published(userId, limit, offset);
        };
    }

    public DataFetcher postPublishedCount() {
        return env -> {
            Long userId = env.getArgument("userId");
            return postService.publishedCount(userId);
        };
    }

    public DataFetcher postLiked() {
        return env -> {
            Long userId = env.getArgument("userId");
            Long limit = env.getArgument("limit");
            Long offset = env.getArgument("offset");
            return postService.liked(userId, limit, offset);
        };
    }

    public DataFetcher postLikedCount() {
        return env -> {
            Long userId = env.getArgument("userId");
            return postService.likedCount(userId);
        };
    }

    public DataFetcher postFollowing() {
        return env -> {
            Long limit = env.getArgument("limit");
            Long beforeId = env.getArgument("beforeId");
            Long afterId = env.getArgument("afterId");
            return postService.following(limit, beforeId, afterId);
        };
    }

    public DataFetcher postFollowingCount() {
        return env -> postService.followingCount();
    }

    public DataFetcher fileInfo() {
        return env -> {
            Long id = env.getArgument("id");
            return fileService.info(id);
        };
    }
}
