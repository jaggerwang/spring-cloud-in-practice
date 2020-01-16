package net.jaggerwang.scip.common.usecase.port.service.async;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;
import reactor.core.publisher.Mono;

public interface StatService {
    Mono<UserStatDto> ofUser(Long userId);

    Mono<PostStatDto> ofPost(Long postId);
}
