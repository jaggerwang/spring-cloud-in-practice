package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDataFetcher extends AbstractDataFetcher {
    public DataFetcher avatar() {
        return env -> {
            UserDto userDto = env.getSource();
            if (userDto.getAvatarId() == null) return monoWithContext(Mono.empty(), env);
            return monoWithContext(fileAsyncService.info(userDto.getAvatarId()), env);
        };
    }

    public DataFetcher stat() {
        return env -> {
            var user = env.getSource();
            UserDto userDto = env.getSource();
            return monoWithContext(statAsyncService.ofUser(userDto.getId()), env);
        };
    }

    public DataFetcher following() {
        return env -> {
            UserDto userDto = env.getSource();
            return monoWithContext(userAsyncService.isFollowing(userDto.getId()), env);
        };
    }
}
