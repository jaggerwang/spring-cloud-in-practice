package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.UserBO;

import java.time.LocalDateTime;

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

    public static UserDTO fromEntity(UserBO userBO, Boolean withPassword) {
        var builder = UserDTO.builder().id(userBO.getId()).username(userBO.getUsername())
                .mobile(userBO.getMobile()).email(userBO.getEmail())
                .avatarId(userBO.getAvatarId()).intro(userBO.getIntro())
                .createdAt(userBO.getCreatedAt()).updatedAt(userBO.getUpdatedAt());

        if (Boolean.TRUE.equals(withPassword)) builder.password(userBO.getPassword());

        return builder.build();
    }

    public static UserDTO fromEntity(UserBO userBO) {
        return fromEntity(userBO, false);
    }

    public UserBO toEntity() {
        return UserBO.builder().id(id).username(username).password(password).mobile(mobile)
                .email(email).avatarId(avatarId).intro(intro).createdAt(createdAt)
                .updatedAt(updatedAt).build();
    }
}
