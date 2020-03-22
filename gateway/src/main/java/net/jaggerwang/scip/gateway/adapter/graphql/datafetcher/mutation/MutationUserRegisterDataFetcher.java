package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.gateway.api.security.annotation.PermitAll;
import net.jaggerwang.scip.common.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MutationUserRegisterDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    @PermitAll
    public Object get(DataFetchingEnvironment env) {
        var userInput = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
        return monoWithContext(userAsyncService.register(userInput), env);
    }
}
