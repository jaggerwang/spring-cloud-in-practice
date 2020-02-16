package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDataFetcher extends AbstractDataFetchers {
    public DataFetcher avatar() {
        return env -> {
            UserDto userDto = env.getSource();
            if (userDto.getAvatarId() == null) {
                return Mono.empty();
            }

            return fileService.info(userDto.getAvatarId());
        };
    }

    public DataFetcher stat() {
        return env -> {
            UserDto userDto = env.getSource();
            return statService.ofUser(userDto.getId());
        };
    }

    public DataFetcher following() {
        return env -> {
            UserDto userDto = env.getSource();
            return userService.isFollowing(userDto.getId());
        };
    }
}
