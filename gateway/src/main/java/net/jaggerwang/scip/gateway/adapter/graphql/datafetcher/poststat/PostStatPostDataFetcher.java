package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.poststat;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.PostStatEntity;
import org.springframework.stereotype.Component;

@Component
public class PostStatPostDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        PostStatDto postStatDto = env.getSource();
        return monoWithContext(postAsyncService.info(postStatDto.getPostId()), env);
    }
}
