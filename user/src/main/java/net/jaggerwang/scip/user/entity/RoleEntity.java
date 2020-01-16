package net.jaggerwang.scip.user.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
