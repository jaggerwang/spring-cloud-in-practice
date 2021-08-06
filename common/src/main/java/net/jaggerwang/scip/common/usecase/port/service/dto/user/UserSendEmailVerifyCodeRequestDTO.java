package net.jaggerwang.scip.common.usecase.port.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jagger Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSendEmailVerifyCodeRequestDTO {
    private String type;

    private String email;
}
