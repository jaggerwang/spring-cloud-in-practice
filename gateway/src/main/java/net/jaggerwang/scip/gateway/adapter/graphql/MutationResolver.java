package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MutationResolver extends AbstractResolver implements GraphQLMutationResolver {
    @PermitAll
    public Mono<UserDto> userRegister(UserDto userInput) {
        return userService.register(userInput);
    }

    public Mono<UserDto> userModify(UserDto userInput, String code) {
        return userService.modify(userInput, code);
    }

    public Mono<String> userSendMobileVerifyCode(String type, String mobile) {
        return userService.sendMobileVerifyCode(type, mobile);
    }

    public Mono<Void> userFollow(Long userId) {
        return userService.follow(userId);
    }

    public Mono<Void> userUnfollow(Long userId) {
        return userService.unfollow(userId);
    }

    public Mono<PostDto> postPublish(PostDto postInput) {
        return postService.publish(postInput);
    }

    public Mono<Void> postDelete(Long id) {
        return postService.delete(id);
    }

    public Mono<Void> postLike(Long postId) {
        return postService.like(postId);
    }

    public Mono<Void> postUnlike(Long postId) {
        return postService.unlike(postId);
    }
}
