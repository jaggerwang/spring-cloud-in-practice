package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.query;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryPostLikedDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var userId = env.getArgument("userId") != null ?
                Long.valueOf((Integer) env.getArgument("userId")) : null;
        var limit = env.getArgument("limit") != null ?
                Long.valueOf((Integer) env.getArgument("limit")) : null;
        var offset = env.getArgument("offset") != null ?
                Long.valueOf((Integer) env.getArgument("offset")) : null;
        return monoWithContext(postAsyncService.liked(userId, limit, offset), env);
    }
}
