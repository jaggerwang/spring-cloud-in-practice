package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.UserBO;

import java.time.LocalDateTime;

/**
 * @author Jagger Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String password;

    private String mobile;

    private String email;

    private Long avatarId;

    private String intro;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserDTO fromBO(UserBO userBO) {
        var builder = UserDTO.builder()
                .id(userBO.getId())
                .username(userBO.getUsername())
                .password(userBO.getPassword())
                .mobile(userBO.getMobile())
                .email(userBO.getEmail())
                .avatarId(userBO.getAvatarId())
                .intro(userBO.getIntro())
                .createdAt(userBO.getCreatedAt())
                .updatedAt(userBO.getUpdatedAt());

        return builder.build();
    }

    public UserBO toBO() {
        return UserBO.builder()
                .id(id)
                .username(username)
                .password(password)
                .mobile(mobile)
                .email(email)
                .avatarId(avatarId)
                .intro(intro)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
