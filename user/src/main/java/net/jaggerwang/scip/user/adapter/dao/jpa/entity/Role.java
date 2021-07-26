package net.jaggerwang.scip.user.adapter.dao.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.RoleBO;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Jagger Wang
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Role fromBO(RoleBO roleBO) {
        return Role.builder().id(roleBO.getId()).name(roleBO.getName()).createdAt(roleBO.getCreatedAt())
                .updatedAt(roleBO.getUpdatedAt()).build();
    }

    public RoleBO toBO() {
        return RoleBO.builder().id(id).name(name).createdAt(createdAt).updatedAt(updatedAt).build();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
