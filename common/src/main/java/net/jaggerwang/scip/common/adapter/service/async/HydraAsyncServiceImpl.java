package net.jaggerwang.scip.common.adapter.service.async;

import net.jaggerwang.scip.common.usecase.port.service.async.HydraAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class HydraAsyncServiceImpl extends AsyncService implements HydraAsyncService {
    public HydraAsyncServiceImpl(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory) {
        super(webClient, cbFactory);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("slow");
    }

    @Override
    public Mono<LoginRequestDto> getLoginRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login")
                .queryParam("login_challenge", challenge)
                .build();
        return get(uri, null)
                .flatMap(response -> response.bodyToMono(LoginRequestDto.class));
    }

    @Override
    public Mono<String> directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/accept")
                .queryParam("login_challenge", challenge)
                .build();
        return put(uri, Map.of("subject", accept.getSubject()), null)
                .flatMap(response -> response.
                        bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> acceptLoginRequest(String challenge, LoginAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/accept")
                .queryParam("login_challenge", challenge)
                .build();
        return put(uri, Map.of("subject", accept.getSubject(), "remember", accept.getRemember(),
                "remember_for", accept.getRememberFor()), null)
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> rejectLoginRequest(String challenge, LoginRejectDto reject) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/login/reject")
                .queryParam("login_challenge", challenge)
                .build();
        return put(uri, Map.of("error", reject.getError(),
                "error_description", reject.getErrorDescription()), null)
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<ConsentRequestDto> getConsentRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent")
                .queryParam("consent_challenge", challenge)
                .build();
        return get(uri, null)
                .flatMap(response -> response.bodyToMono(ConsentRequestDto.class));
    }

    @Override
    public Mono<String> directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/accept")
                .queryParam("consent_challenge", challenge)
                .build();
        return put(uri, Map.of("grant_scope", accept.getGrantScope(),
                "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                "session", accept.getSession()), null)
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> acceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/accept")
                .queryParam("consent_challenge", challenge)
                .build();
        return put(uri, Map.of("grant_scope", accept.getGrantScope(),
                "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                "session", accept.getSession(), "remember", accept.getRemember(),
                "remember_for", accept.getRememberFor()), null)
                .flatMap(response -> response.
                        bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> rejectConsentRequest(String challenge, ConsentRejectDto reject) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/consent/reject")
                .queryParam("consent_challenge", challenge)
                .build();
        return put(uri, Map.of("error", reject.getError(),
                "error_description", reject.getErrorDescription()), null)
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<LogoutRequestDto> getLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout")
                .queryParam("logout_challenge", challenge)
                .build();
        return get(uri, null)
                .flatMap(response -> response.bodyToMono(LogoutRequestDto.class));
    }

    @Override
    public Mono<String> acceptLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout/accept")
                .queryParam("logout_challenge", challenge)
                .build();
        return put(uri, null, null)
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<Void> rejectLogoutRequest(String challenge) {
        var uri = new DefaultUriBuilderFactory().builder()
                .path("/oauth2/auth/requests/logout/reject")
                .queryParam("logout_challenge", challenge)
                .build();
        return put(uri, null, null)
                .then();
    }
}
