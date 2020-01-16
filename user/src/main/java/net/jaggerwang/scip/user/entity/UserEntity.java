package net.jaggerwang.scip.user.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @NonNull
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String mobile;

    private String email;

    private Long avatarId;

    @NonNull
    private String intro;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
