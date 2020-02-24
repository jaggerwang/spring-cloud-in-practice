package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryDataFetcher extends AbstractDataFetcher {
    public DataFetcher userLogged() {
        return env -> userAsyncService.logged().toFuture();
    }

    public DataFetcher userInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return userAsyncService.info(id).toFuture();
        };
    }

    public DataFetcher userFollowing() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return userAsyncService.following(userId, limit, offset).toFuture();
        };
    }

    public DataFetcher userFollowingCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return userAsyncService.followingCount(userId).toFuture();
        };
    }

    public DataFetcher userFollower() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return userAsyncService.follower(userId, limit, offset).toFuture();
        };
    }

    public DataFetcher userFollowerCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return userAsyncService.followerCount(userId).toFuture();
        };
    }

    public DataFetcher postInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return postAsyncService.info(id).toFuture();
        };
    }

    public DataFetcher postPublished() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return postAsyncService.published(userId, limit, offset).toFuture();
        };
    }

    public DataFetcher postPublishedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return postAsyncService.publishedCount(userId).toFuture();
        };
    }

    public DataFetcher postLiked() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return postAsyncService.liked(userId, limit, offset).toFuture();
        };
    }

    public DataFetcher postLikedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return postAsyncService.likedCount(userId).toFuture();
        };
    }

    public DataFetcher postFollowing() {
        return env -> {
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var beforeId = Long.valueOf((Integer) env.getArgument("beforeId"));
            var afterId = Long.valueOf((Integer) env.getArgument("afterId"));
            return postAsyncService.following(limit, beforeId, afterId).toFuture();
        };
    }

    public DataFetcher postFollowingCount() {
        return env -> postAsyncService.followingCount().toFuture();
    }

    public DataFetcher fileInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return fileAsyncService.info(id).toFuture();
        };
    }
}
