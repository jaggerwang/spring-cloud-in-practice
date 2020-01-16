package net.jaggerwang.scip.common.adapter.service.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import net.jaggerwang.scip.common.usecase.port.service.sync.HydraService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class HydraSyncService extends SyncService implements HydraService {
    protected ObjectMapper objectMapper;

    public HydraSyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                            ObjectMapper objectMapper) {
        super(restTemplate, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("slow");
    }

    @Override
    public LoginRequestDto getLoginRequest(String challenge) {
        return get("/oauth2/auth/requests/login", Map.of("login_challenge", challenge),
                    LoginRequestDto.class)
                .getBody();
    }

    @Override
    public String directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept) {
        return (String) put("/oauth2/auth/requests/login/accept",
                    Map.of("login_challenge", challenge), Map.of("subject", accept.getSubject()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String acceptLoginRequest(String challenge, LoginAcceptDto accept) {
        return (String) put("/oauth2/auth/requests/login/accept",
                    Map.of("login_challenge", challenge),
                    Map.of("subject", accept.getSubject(), "remember", accept.getRemember(),
                        "remember_for", accept.getRememberFor()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String rejectLoginRequest(String challenge, LoginRejectDto reject) {
        return (String) put("/oauth2/auth/requests/login/reject",
                    Map.of("login_challenge", challenge),
                    Map.of("error", reject.getError(),
                            "error_description", reject.getErrorDescription()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public ConsentRequestDto getConsentRequest(String challenge) {
        return get("/oauth2/auth/requests/consent", Map.of("consent_challenge", challenge),
                    ConsentRequestDto.class)
                .getBody();
    }

    @Override
    public String directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        return (String) put("/oauth2/auth/requests/consent/accept",
                    Map.of("consent_challenge", challenge),
                    Map.of("grant_scope", accept.getGrantScope(),
                            "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                            "session", accept.getSession()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String acceptConsentRequest(String challenge, ConsentAcceptDto accept) {
        return (String) put("/oauth2/auth/requests/consent/accept",
                    Map.of("consent_challenge", challenge),
                    Map.of("grant_scope", accept.getGrantScope(),
                            "grant_access_token_audience", accept.getGrantAccessTokenAudience(),
                            "session", accept.getSession(),
                            "remember", accept.getRemember(),
                            "remember_for", accept.getRememberFor()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public String rejectConsentRequest(String challenge, ConsentRejectDto reject) {
        return (String) put("/oauth2/auth/requests/consent/reject",
                    Map.of("consent_challenge", challenge),
                    Map.of("error", reject.getError(),
                            "error_description", reject.getErrorDescription()),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public LogoutRequestDto getLogoutRequest(String challenge) {
        return get("/oauth2/auth/requests/logout", Map.of("logout_challenge", challenge),
                    LogoutRequestDto.class)
                .getBody();
    }

    @Override
    public String acceptLogoutRequest(String challenge) {
        return (String) put("/oauth2/auth/requests/logout/accept",
                    Map.of("logout_challenge", challenge),
                    new ParameterizedTypeReference<Map<String, Object>>() {})
                .getBody()
                .get("redirect_to");
    }

    @Override
    public Void rejectLogoutRequest(String challenge) {
        return put("/oauth2/auth/requests/logout/reject", Map.of("logout_challenge", challenge),
                    Void.class)
                .getBody();
    }
}
