package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import reactor.core.publisher.Mono;

public interface StatAsyncService {
    Mono<UserStatDTO> ofUser(Long userId);

    Mono<PostStatDTO> ofPost(Long postId);
}
