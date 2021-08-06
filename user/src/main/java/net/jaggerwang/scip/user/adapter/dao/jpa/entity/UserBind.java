package net.jaggerwang.scip.user.adapter.dao.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Jagger Wang
 */
@Entity
@Table(name = "user_bind")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_auth_provider")
    private String externalAuthProvider;

    @Column(name = "external_user_id")
    private String externalUserId;

    @Column(name = "internal_user_id")
    private Long internalUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
