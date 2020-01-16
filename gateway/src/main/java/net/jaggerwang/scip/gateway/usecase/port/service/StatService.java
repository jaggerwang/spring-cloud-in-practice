package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.gateway.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserStatDto;
import reactor.core.publisher.Mono;

public interface StatService {
    Mono<UserStatDto> ofUser(Long userId);

    Mono<PostStatDto> ofPost(Long postId);
}
