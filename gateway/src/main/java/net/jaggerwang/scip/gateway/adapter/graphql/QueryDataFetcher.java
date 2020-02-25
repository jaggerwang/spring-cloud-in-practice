package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryDataFetcher extends AbstractDataFetcher {
    public DataFetcher userLogged() {
        return env -> monoWithContext(userAsyncService.logged(), env);
    }

    public DataFetcher userInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return monoWithContext(userAsyncService.info(id), env);
        };
    }

    public DataFetcher userFollowing() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return monoWithContext(userAsyncService.following(userId, limit, offset), env);
        };
    }

    public DataFetcher userFollowingCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(userAsyncService.followingCount(userId), env);
        };
    }

    public DataFetcher userFollower() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return monoWithContext(userAsyncService.follower(userId, limit, offset), env);
        };
    }

    public DataFetcher userFollowerCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(userAsyncService.followerCount(userId), env);
        };
    }

    public DataFetcher postInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return monoWithContext(postAsyncService.info(id), env);
        };
    }

    public DataFetcher postPublished() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return monoWithContext(postAsyncService.published(userId, limit, offset), env);
        };
    }

    public DataFetcher postPublishedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(postAsyncService.publishedCount(userId), env);
        };
    }

    public DataFetcher postLiked() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var offset = Long.valueOf((Integer) env.getArgument("offset"));
            return monoWithContext(postAsyncService.liked(userId, limit, offset), env);
        };
    }

    public DataFetcher postLikedCount() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(postAsyncService.likedCount(userId), env);
        };
    }

    public DataFetcher postFollowing() {
        return env -> {
            var limit = Long.valueOf((Integer) env.getArgument("limit"));
            var beforeId = Long.valueOf((Integer) env.getArgument("beforeId"));
            var afterId = Long.valueOf((Integer) env.getArgument("afterId"));
            return monoWithContext(postAsyncService.following(limit, beforeId, afterId), env);
        };
    }

    public DataFetcher postFollowingCount() {
        return env -> monoWithContext(postAsyncService.followingCount(), env);
    }

    public DataFetcher fileInfo() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return monoWithContext(fileAsyncService.info(id), env);
        };
    }
}
