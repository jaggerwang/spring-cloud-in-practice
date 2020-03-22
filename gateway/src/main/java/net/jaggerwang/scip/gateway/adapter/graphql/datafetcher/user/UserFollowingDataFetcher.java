package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.user;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserFollowingDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        UserDto userDto = env.getSource();
        return monoWithContext(userAsyncService.isFollowing(userDto.getId()), env);
    }
}
