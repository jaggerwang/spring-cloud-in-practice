package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class MutationPostPublishDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var postInput = objectMapper.convertValue(env.getArgument("post"), PostDto.class);
        return monoWithContext(postAsyncService.publish(postInput), env);
    }
}
