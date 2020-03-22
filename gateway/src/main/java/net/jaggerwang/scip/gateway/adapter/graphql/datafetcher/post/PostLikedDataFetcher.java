package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.post;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostLikedDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        PostDto postDto = env.getSource();
        return monoWithContext(postAsyncService.isLiked(postDto.getId()), env);
    }
}
