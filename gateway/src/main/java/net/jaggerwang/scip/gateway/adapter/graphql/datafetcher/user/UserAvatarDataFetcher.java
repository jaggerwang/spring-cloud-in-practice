package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.user;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserAvatarDataFetcher extends AbstractDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        UserDto userDto = env.getSource();
        if (userDto.getAvatarId() == null) return Mono.empty();
        return fileAsyncService.info(userDto.getAvatarId());
    }
}
