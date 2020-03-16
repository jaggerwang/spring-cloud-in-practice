package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class IndexController {
    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    private UserAsyncService userAsyncService;

    public IndexController(ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
                           UserAsyncService userAsyncService) {
        this.authorizedClientRepository = authorizedClientRepository;
        this.userAsyncService = userAsyncService;
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

//    @PostMapping("/login")
//    public Mono<RootDto> login(@RequestBody UserDto userDto) {
//        return userAsyncService.verifyPassword(userDto)
//                .map(verifiedUser -> {
//                    var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
//                                    userDto.getUsername(), userDto.getPassword()));
//                    var sc = SecurityContextHolder.getContext();
//                    sc.setAuthentication(auth);
//
//                    return new RootDto().addDataEntry("user", verifiedUser);
//                });
//    }
}
