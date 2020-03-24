package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MutationUserFollowDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var userId = Long.valueOf((Integer) env.getArgument("userId"));
        return userAsyncService.follow(userId).then(Mono.just(true));
    }
}
