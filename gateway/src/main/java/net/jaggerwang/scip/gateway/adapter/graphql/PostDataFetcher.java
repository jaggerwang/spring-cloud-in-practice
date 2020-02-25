package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PostDataFetcher extends AbstractDataFetcher {
    public DataFetcher user() {
        return env -> {
            PostDto postDto = env.getSource();
            return monoWithContext(userAsyncService.info(postDto.getUserId()), env);
        };
    }

    public DataFetcher images() {
        return env -> {
            PostDto postDto = env.getSource();
            return monoWithContext(fileAsyncService.infos(postDto.getImageIds(), false), env);
        };
    }

    public DataFetcher video() {
        return env -> {
            PostDto postDto = env.getSource();
            if (postDto.getVideoId() == null) return monoWithContext(Mono.empty(), env);
            return monoWithContext(fileAsyncService.info(postDto.getVideoId()), env);
        };
    }

    public DataFetcher stat() {
        return env -> {
            PostDto postDto = env.getSource();
            return monoWithContext(statAsyncService.ofPost(postDto.getId()), env);
        };
    }

    public DataFetcher liked() {
        return env -> {
            PostDto postDto = env.getSource();
            return monoWithContext(postAsyncService.isLiked(postDto.getId()), env);
        };
    }
}
