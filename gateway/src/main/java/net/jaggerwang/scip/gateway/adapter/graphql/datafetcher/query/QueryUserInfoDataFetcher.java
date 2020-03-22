package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.query;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;

@Component
public class QueryUserInfoDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var id = Long.valueOf((Integer) env.getArgument("id"));
        return monoWithContext(userAsyncService.info(id), env);
    }
}
