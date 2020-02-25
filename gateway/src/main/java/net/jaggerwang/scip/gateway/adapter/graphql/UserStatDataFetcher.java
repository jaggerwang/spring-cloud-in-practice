package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;
import org.springframework.stereotype.Component;

@Component
public class UserStatDataFetcher extends AbstractDataFetcher {
    public DataFetcher user() {
        return env -> {
            UserStatDto userStatDto = env.getSource();
            return monoWithContext(userAsyncService.info(userStatDto.getUserId()), env);
        };
    }
}
