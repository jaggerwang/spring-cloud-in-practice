package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.post;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.PostEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PostImagesDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        PostDto postDto = env.getSource();
        if (postDto.getImageIds().isEmpty()) return monoWithContext(Mono.just(List.of()), env);
        return monoWithContext(fileAsyncService.infos(postDto.getImageIds(), false), env);
    }
}
