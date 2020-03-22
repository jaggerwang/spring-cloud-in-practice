package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.post;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.PostEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class PostVideoDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        PostDto postDto = env.getSource();
        if (postDto.getVideoId() == null) return monoWithContext(Mono.empty(), env);
        return monoWithContext(fileAsyncService.info(postDto.getVideoId()), env);
    }
}
