package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.async.HydraService;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class HydraAsyncService extends AsyncService implements HydraService {
    protected ObjectMapper objectMapper;

    public HydraAsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                             ObjectMapper objectMapper) {
        super(webClient, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("slow");
    }

    @Override
    public Mono<LoginRequestDto> getLoginRequest(String challenge) {
        return get("/oauth2/auth/requests/login", Map.of("login_challenge", challenge))
                .flatMap(response -> response.bodyToMono(LoginRequestDto.class));
    }

    @Override
    public Mono<String> directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept) {
        return put("/oauth2/auth/requests/login/accept", Map.of("login_challenge", challenge),
                    Map.of("subject", accept.getSubject()))
                .flatMap(response -> response.
                        bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> acceptLoginRequest(String challenge, LoginAcceptDto accept) {
        return put("/oauth2/auth/requests/login/accept", Map.of("login_challenge", challenge),
                    Map.of("subject", accept.getSubject(), "remember", accept.getRemember(),
                        "remember_for", accept.getRememberFor()))
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> rejectLoginRequest(String challenge, LoginRejectDto reject) {
        return put("/oauth2/auth/requests/login/reject", Map.of("login_challenge", challenge),
                    Map.of("error", reject.getError(),
                            "error_description", reject.getErrorDescription()))
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<ConsentRequestDto> getConsentRequest(String challenge) {
        return get("/oauth2/auth/requests/consent", Map.of("consent_challenge", challenge))
                .flatMap(response -> response.bodyToMono(ConsentRequestDto.class));
    }

    @Override
    public Mono<String> directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        return put("/oauth2/auth/requests/consent/accept", Map.of("consent_challenge", challenge),
                    Map.of("grant_scope", accept.getGrantScope(),
                            "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                            "session", accept.getSession()))
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> acceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        return put("/oauth2/auth/requests/consent/accept", Map.of("consent_challenge", challenge),
                Map.of("grant_scope", accept.getGrantScope(),
                        "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                        "session", accept.getSession(),
                        "remember", accept.getRemember(),
                        "remember_for", accept.getRememberFor()))
                .flatMap(response -> response.
                        bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<String> rejectConsentRequest(String challenge, ConsentRejectDto reject) {
        return put("/oauth2/auth/requests/consent/reject", Map.of("consent_challenge", challenge),
                    Map.of("error", reject.getError(),
                            "error_description", reject.getErrorDescription()))
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<LogoutRequestDto> getLogoutRequest(String challenge) {
        return get("/oauth2/auth/requests/logout", Map.of("logout_challenge", challenge))
                .flatMap(response -> response.bodyToMono(LogoutRequestDto.class));
    }

    @Override
    public Mono<String> acceptLogoutRequest(String challenge) {
        return put("/oauth2/auth/requests/logout/accept", Map.of("logout_challenge", challenge))
                .flatMap(response -> response
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}))
                .map(m -> (String) m.get("redirect_to"));
    }

    @Override
    public Mono<Void> rejectLogoutRequest(String challenge) {
        return put("/oauth2/auth/requests/logout/reject", Map.of("logout_challenge", challenge))
                .then();
    }
}
