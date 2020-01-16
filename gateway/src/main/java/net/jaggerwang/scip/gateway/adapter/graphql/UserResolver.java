package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.FileDto;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserStatDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserResolver extends AbstractResolver implements GraphQLResolver<UserDto> {
    public Mono<FileDto> avatar(UserDto userDto) {
        if (userDto.getAvatarId() == null) {
            return Mono.empty();
        }

        return fileService.info(userDto.getAvatarId());
    }

    public Mono<UserStatDto> stat(UserDto userDto) {
        return statService.ofUser(userDto.getId());
    }

    public Mono<Boolean> following(UserDto userDto) {
        return userService.isFollowing(userDto.getId());
    }
}
