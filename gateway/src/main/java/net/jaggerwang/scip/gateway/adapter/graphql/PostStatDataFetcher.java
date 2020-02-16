package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import org.springframework.stereotype.Component;

@Component
public class PostStatDataFetcher extends AbstractDataFetchers {
    public DataFetcher post() {
        return env -> {
            PostStatDto postStatDto = env.getSource();
            return postService.info(postStatDto.getPostId());
        };
    }
}
