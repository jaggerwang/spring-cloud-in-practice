package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.RoleBO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static RoleDTO fromEntity(RoleBO roleBO) {
        return RoleDTO.builder().id(roleBO.getId()).name(roleBO.getName())
                .createdAt(roleBO.getCreatedAt()).updatedAt(roleBO.getUpdatedAt()).build();
    }

    public RoleBO toEntity() {
        return RoleBO.builder().id(id).name(name).createdAt(createdAt).updatedAt(updatedAt)
                .build();
    }
}
