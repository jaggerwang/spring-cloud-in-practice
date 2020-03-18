package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
public class MutationDataFetcher extends AbstractDataFetcher {
    public DataFetcher authLogin() {
        return env -> {
            var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
            String username = null;
            if (userInput.getUsername() != null)  {
                username = userInput.getUsername();
            } else if (userInput.getMobile() != null) {
                username = userInput.getMobile();
            } else if (userInput.getEmail() != null) {
                username = userInput.getEmail();
            }
            if (StringUtils.isEmpty(username)) {
                throw new UsecaseException("用户名、手机或邮箱不能都为空");
            }
            var password = userInput.getPassword();
            if (StringUtils.isEmpty(password)) {
                throw new UsecaseException("密码不能为空");
            }

            return monoWithContext(loginUser(username, password)
                    .flatMap(loggedUser -> userAsyncService.info(loggedUser.getId())), env);
        };
    }

    public DataFetcher userRegister() {
        return env -> {
            var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
            return monoWithContext(userAsyncService.register(userInput), env);
        };
    }

    public DataFetcher userModify() {
        return env -> {
            var user = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
            String code = env.getArgument("code");
            return code != null ?
                    monoWithContext(userAsyncService.modify(user, code), env) :
                    monoWithContext(userAsyncService.modify(user), env);
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
            return monoWithContext(userAsyncService.follow(userId).then(Mono.just(true)), env);
        };
    }

    public DataFetcher userUnfollow() {
        return env -> {
            var userId = Long.valueOf((Integer) env.getArgument("userId"));
            return monoWithContext(userAsyncService.unfollow(userId).then(Mono.just(true)), env);
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
            return monoWithContext(postAsyncService.delete(id).then(Mono.just(true)), env);
        };
    }

    public DataFetcher postLike() {
        return env -> {
            var postId = Long.valueOf((Integer) env.getArgument("postId"));
            return monoWithContext(postAsyncService.like(postId).then(Mono.just(true)), env);
        };
    }

    public DataFetcher postUnlike() {
        return env -> {
            var postId = Long.valueOf((Integer) env.getArgument("postId"));
            return monoWithContext(postAsyncService.unlike(postId).then(Mono.just(true)), env);
        };
    }
}
