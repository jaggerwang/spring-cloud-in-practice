package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PostStatResolver extends AbstractResolver implements GraphQLResolver<PostStatDto> {
    public Mono<PostDto> post(PostStatDto postStatDto) {
        return postService.info(postStatDto.getPostId());
    }
}
