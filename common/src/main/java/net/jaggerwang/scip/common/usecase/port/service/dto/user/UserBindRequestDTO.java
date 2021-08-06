package net.jaggerwang.scip.common.usecase.port.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;

/**
 * @author Jagger Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBindRequestDTO {
    private String externalAuthProvider;

    private String externalUserId;

    private UserDTO internalUser;
}
