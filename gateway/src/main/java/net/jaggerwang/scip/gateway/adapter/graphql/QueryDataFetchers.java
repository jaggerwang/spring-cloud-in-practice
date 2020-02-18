package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryDataFetchers extends AbstractDataFetchers {
    public DataFetcher userLogged() {
        return env -> userAsyncService.logged();
    }

    public DataFetcher userInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return userAsyncService.info(id);
        };
    }

    public DataFetcher userFollowing() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return userAsyncService.following(userId, limit, offset);
        };
    }

    public DataFetcher userFollowingCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return userAsyncService.followingCount(userId);
        };
    }

    public DataFetcher userFollower() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return userAsyncService.follower(userId, limit, offset);
        };
    }

    public DataFetcher userFollowerCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return userAsyncService.followerCount(userId);
        };
    }

    public DataFetcher postInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return postAsyncService.info(id);
        };
    }

    public DataFetcher postPublished() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return postAsyncService.published(userId, limit, offset);
        };
    }

    public DataFetcher postPublishedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return postAsyncService.publishedCount(userId);
        };
    }

    public DataFetcher postLiked() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return postAsyncService.liked(userId, limit, offset);
        };
    }

    public DataFetcher postLikedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return postAsyncService.likedCount(userId);
        };
    }

    public DataFetcher postFollowing() {
        return env -> {
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var beforeId = Long.valueOf((Integer) env.getArgument("beforeId"));
            var afterId = Long.valueOf((Integer) env.getArgument("afterId"));
            return postAsyncService.following(limit, beforeId, afterId);
        };
    }

    public DataFetcher postFollowingCount() {
        return env -> postAsyncService.followingCount();
    }

    public DataFetcher fileInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return fileAsyncService.info(id);
        };
    }
}
