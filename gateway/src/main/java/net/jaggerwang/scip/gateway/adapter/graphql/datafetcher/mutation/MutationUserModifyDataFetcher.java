package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.UserEntity;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import org.springframework.stereotype.Component;

@Component
public class MutationUserModifyDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        var user = objectMapper.convertValue(env.getArgument("user"), UserDto.class);
        String code = env.getArgument("code");
        return code != null ?
                monoWithContext(userAsyncService.modify(user, code), env) :
                monoWithContext(userAsyncService.modify(user), env);
    }
}
