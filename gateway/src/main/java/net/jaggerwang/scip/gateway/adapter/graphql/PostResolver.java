package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PostResolver extends AbstractResolver implements GraphQLResolver<PostDto> {
    public Mono<UserDto> user(PostDto postDto) {
        return userService.info(postDto.getUserId());
    }

    public Mono<List<FileDto>> images(PostDto postDto) {
        return fileService.infos(postDto.getImageIds(), false);
    }

    public Mono<FileDto> video(PostDto postDto) {
        if (postDto.getVideoId() == null) {
            return Mono.empty();
        }

        return fileService.info(postDto.getVideoId());
    }

    public Mono<PostStatDto> stat(PostDto postDto) {
        return statService.ofPost(postDto.getId());
    }

    public Mono<Boolean> liked(PostDto postDto) {
        return postService.isLiked(postDto.getId());
    }
}
