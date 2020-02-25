package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class MutationDataFetcher extends AbstractDataFetcher {
    public DataFetcher userRegister() {
        return env -> {
            var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
            return monoWithContext(userAsyncService.register(userInput), env);
        };
    }

    public DataFetcher userModify() {
        return env -> {
            var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
            String code = env.getArgument("code");
            return monoWithContext(userAsyncService.modify(userInput, code), env);
        };
    }

    public DataFetcher userSendMobileVerifyCode() {
        return env -> {
            String type = env.getArgument("type");
            String mobile = env.getArgument("mobile");
            return monoWithContext(userAsyncService.sendMobileVerifyCode(type, mobile), env);
        };
    }

    public DataFetcher userFollow() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(userAsyncService.follow(userId), env);
        };
    }

    public DataFetcher userUnfollow() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(userAsyncService.unfollow(userId), env);
        };
    }

    public DataFetcher postPublish() {
        return env -> {
            var postInput = objectMapper.convertValue(env.getArgument("post"), PostDto.class);
            return monoWithContext(postAsyncService.publish(postInput), env);
        };
    }

    public DataFetcher postDelete() {
        return env -> {
            var id = Long.valueOf((Integer) env.getArgument("id"));
            return monoWithContext(postAsyncService.delete(id), env);
        };
    }

    public DataFetcher postLike() {
        return env -> {
            var postId = Long.valueOf((Integer) env.getArgument("postId"));
            return monoWithContext(postAsyncService.like(postId), env);
        };
    }

    public DataFetcher postUnlike() {
        return env -> {
            var postId = Long.valueOf((Integer) env.getArgument("postId"));
            return monoWithContext(postAsyncService.unlike(postId), env);
        };
    }
}
