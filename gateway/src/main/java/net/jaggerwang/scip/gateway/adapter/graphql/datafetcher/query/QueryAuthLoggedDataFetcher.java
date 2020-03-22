package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.query;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QueryAuthLoggedDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    @PermitAll
    public Object get(DataFetchingEnvironment env) {
        return monoWithContext(loggedUserId()
                .flatMap(userId -> userAsyncService.info(userId)), env);
    }
}
