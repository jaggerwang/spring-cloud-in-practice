package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;

public interface HydraSyncService {
    LoginRequestDTO getLoginRequest(String challenge);

    String directlyAcceptLoginRequest(String challenge, LoginAcceptDTO accept);

    String acceptLoginRequest(String challenge, LoginAcceptDTO accept);

    String rejectLoginRequest(String challenge, LoginRejectDTO reject);

    ConsentRequestDTO getConsentRequest(String challenge);

    String directlyAcceptConsentRequest(String challenge, ConsentAcceptDTO accept);

    String acceptConsentRequest(String challenge, ConsentAcceptDTO accept);

    String rejectConsentRequest(String challenge, ConsentRejectDTO reject);

    LogoutRequestDTO getLogoutRequest(String challenge);

    String acceptLogoutRequest(String challenge);

    Void rejectLogoutRequest(String challenge);
}
