package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserStatResolver extends AbstractResolver implements GraphQLResolver<UserStatDto> {
    public Mono<UserDto> user(UserStatDto userStatDto) {
        return userService.info(userStatDto.getUserId());
    }
}
