package net.jaggerwang.scip.gateway.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.sync.UserSyncService;
import net.jaggerwang.scip.common.adapter.service.async.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class IndexController {
    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    private UserSyncService userSyncService;
    private UserAsyncService userAsyncService;

    public IndexController(ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
                           RestTemplate restTemplate,
                           CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper,
                           WebClient webClient, ReactiveCircuitBreakerFactory rcbFactory) {
        this.authorizedClientRepository = authorizedClientRepository;
        this.userSyncService = new UserSyncService(restTemplate, cbFactory, objectMapper);
        this.userAsyncService = new UserAsyncService(webClient, rcbFactory, objectMapper);
    }

    @GetMapping("/")
    public Mono<RootDto> index(ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(token -> authorizedClientRepository
                        .loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token, exchange)
                        .cast(OAuth2AuthorizedClient.class))
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map((accessToken) -> new RootDto().addDataEntry("accessToken", accessToken))
                .defaultIfEmpty(new RootDto().addDataEntry("accessToken", null));
    }

    @GetMapping("/sync/userLogged")
    public Optional<UserDto> syncUserLogged() {
        return userSyncService.logged();
    }

    @GetMapping("/sync/userInfo")
    public UserDto syncUserInfo() {
        return userSyncService.info(Long.valueOf(1));
    }

    @GetMapping("/async/userLogged")
    public Mono<UserDto> asyncUserLogged() {
        return userAsyncService.logged();
    }

    @GetMapping("/async/userInfo")
    public Mono<UserDto> asyncUserInfo() {
        return userAsyncService.info(Long.valueOf(1));
    }
}
