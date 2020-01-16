package net.jaggerwang.scip.gateway.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.AsyncInternalService;
import net.jaggerwang.scip.gateway.usecase.port.service.StatService;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.UserStatDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Component
public class StatServiceImpl extends AsyncInternalService implements StatService {
    protected ObjectMapper objectMapper;

    public StatServiceImpl(@Qualifier("statServiceWebClient") WebClient webClient,
                           ReactiveCircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(webClient, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<UserStatDto> ofUser(Long userId) {
        return getData("/stat/ofUser", Map.of("userId", userId.toString()))
                .map(data -> objectMapper.convertValue(data.get("userStat"), UserStatDto.class));
    }

    @Override
    public Mono<PostStatDto> ofPost(Long postId) {
        return getData("/stat/ofPost", Map.of("postId", postId.toString()))
                .map(data -> objectMapper.convertValue(data.get("postStat"), PostStatDto.class));
    }
}
