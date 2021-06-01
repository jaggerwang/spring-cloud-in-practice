package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.StatAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class StatAsyncServiceImpl extends InternalAsyncService implements StatAsyncService {
    public StatAsyncServiceImpl(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                                ObjectMapper objectMapper) {
        super(webClient, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<UserStatDTO> ofUser(Long userId) {
        return getData("/stat/ofUser", Map.of("userId", userId.toString()))
                .map(data -> objectMapper.convertValue(data.get("userStat"), UserStatDTO.class));
    }

    @Override
    public Mono<PostStatDTO> ofPost(Long postId) {
        return getData("/stat/ofPost", Map.of("postId", postId.toString()))
                .map(data -> objectMapper.convertValue(data.get("postStat"), PostStatDTO.class));
    }
}
