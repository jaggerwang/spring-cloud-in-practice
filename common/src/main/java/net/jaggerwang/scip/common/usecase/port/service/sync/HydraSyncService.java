package net.jaggerwang.scip.common.usecase.port.service.sync;

import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;

public interface HydraSyncService {
    LoginRequestDto getLoginRequest(String challenge);

    String directlyAcceptLoginRequest(String challenge, LoginAcceptDto accept);

    String acceptLoginRequest(String challenge, LoginAcceptDto accept);

    String rejectLoginRequest(String challenge, LoginRejectDto reject);

    ConsentRequestDto getConsentRequest(String challenge);

    String directlyAcceptConsentRequest(String challenge, ConsentAcceptDto accept);

    String acceptConsentRequest(String challenge, ConsentAcceptDto accept);

    String rejectConsentRequest(String challenge, ConsentRejectDto reject);

    LogoutRequestDto getLogoutRequest(String challenge);

    String acceptLogoutRequest(String challenge);

    Void rejectLogoutRequest(String challenge);
}
