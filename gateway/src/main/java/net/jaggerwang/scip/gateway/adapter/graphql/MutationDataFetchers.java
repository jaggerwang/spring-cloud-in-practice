package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class MutationDataFetchers extends AbstractDataFetchers {
    public DataFetcher userRegister() {
        return env -> {
            UserDto userInput = env.getArgument("userInput");
            return userService.register(userInput);
        };
    }

    public DataFetcher userModify() {
        return env -> {
            UserDto userInput = env.getArgument("userInput");
            String code = env.getArgument("code");
            return userService.modify(userInput, code);
        };
    }

    public DataFetcher userSendMobileVerifyCode() {
        return env -> {
            String type = env.getArgument("type");
            String mobile = env.getArgument("mobile");
            return userService.sendMobileVerifyCode(type, mobile);
        };
    }

    public DataFetcher userFollow() {
        return env -> {
            Long userId = env.getArgument("userId");
            return userService.follow(userId);
        };
    }

    public DataFetcher userUnfollow() {
        return env -> {
            Long userId = env.getArgument("userId");
            return userService.unfollow(userId);
        };
    }

    public DataFetcher postPublish() {
        return env -> {
            PostDto postInput = env.getArgument("postInput");
            return postService.publish(postInput);
        };
    }

    public DataFetcher postDelete() {
        return env -> {
            Long id = env.getArgument("id");
            return postService.delete(id);
        };
    }

    public DataFetcher postLike() {
        return env -> {
            Long postId = env.getArgument("postId");
            return postService.like(postId);
        };
    }

    public DataFetcher postUnlike() {
        return env -> {
            Long postId = env.getArgument("postId");
            return postService.unlike(postId);
        };
    }
}
