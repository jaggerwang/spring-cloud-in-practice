package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import reactor.core.publisher.Mono;

public interface HydraAsyncService {
    Mono<LoginRequestDTO> getLoginRequest(String challenge);

    Mono<String> directlyAcceptLoginRequest(String challenge, LoginAcceptDTO accept);

    Mono<String> acceptLoginRequest(String challenge, LoginAcceptDTO accept);

    Mono<String> rejectLoginRequest(String challenge, LoginRejectDTO reject);

    Mono<ConsentRequestDTO> getConsentRequest(String challenge);

    Mono<String> directlyAcceptConsentRequest(String challenge, ConsentAcceptDTO accept);

    Mono<String> acceptConsentRequest(String challenge, ConsentAcceptDTO accept);

    Mono<String> rejectConsentRequest(String challenge, ConsentRejectDTO reject);

    Mono<LogoutRequestDTO> getLogoutRequest(String challenge);

    Mono<String> acceptLogoutRequest(String challenge);

    Mono<Void> rejectLogoutRequest(String challenge);
}
