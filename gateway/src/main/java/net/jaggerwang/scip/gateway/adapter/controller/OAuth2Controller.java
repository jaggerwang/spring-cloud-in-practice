package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    public OAuth2Controller(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @GetMapping("/accessToken")
    public Mono<RootDto> oauth2Token(ServerWebExchange exchange, WebSession session) {
        return exchange
                .getPrincipal()
                .filter((principal) -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap((token) -> authorizedClientRepository
                        .loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token, exchange)
                        .cast(OAuth2AuthorizedClient.class))
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map((accessToken) -> new RootDto().addDataEntry("accessToken", accessToken))
                .defaultIfEmpty(new RootDto().addDataEntry("accessToken", null));
    }
}
