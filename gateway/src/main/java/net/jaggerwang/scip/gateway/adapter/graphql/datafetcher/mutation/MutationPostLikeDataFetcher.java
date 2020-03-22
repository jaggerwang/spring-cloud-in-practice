package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MutationPostLikeDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var postId = Long.valueOf((Integer) env.getArgument("postId"));
        return monoWithContext(postAsyncService.like(postId).then(Mono.just(true)), env);
    }
}
