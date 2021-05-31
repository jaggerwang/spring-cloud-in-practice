package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import reactor.core.publisher.Mono;

public interface HydraAsyncService {
    Mono<LoginRequestDto> getLoginRequest(String challenge);

    Mono<String> directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept);

    Mono<String> acceptLoginRequest(String challenge, LoginAcceptDto accept);

    Mono<String> rejectLoginRequest(String challenge, LoginRejectDto reject);

    Mono<ConsentRequestDto> getConsentRequest(String challenge);

    Mono<String> directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept);

    Mono<String> acceptConsentRequest(String challenge, ConsentAcceptDto accept);

    Mono<String> rejectConsentRequest(String challenge, ConsentRejectDto reject);

    Mono<LogoutRequestDto> getLogoutRequest(String challenge);

    Mono<String> acceptLogoutRequest(String challenge);

    Mono<Void> rejectLogoutRequest(String challenge);
}
