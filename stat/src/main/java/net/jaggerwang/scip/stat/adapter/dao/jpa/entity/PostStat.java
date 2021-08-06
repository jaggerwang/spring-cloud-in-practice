package net.jaggerwang.scip.stat.adapter.dao.jpa.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.PostStatBO;

/**
 * @author Jagger Wang
 */
@Entity
@Table(name = "post_stat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static PostStat fromBO(PostStatBO fileEntity) {
        return PostStat.builder().id(fileEntity.getId()).postId(fileEntity.getPostId())
                .likeCount(fileEntity.getLikeCount()).createdAt(fileEntity.getCreatedAt())
                .updatedAt(fileEntity.getUpdatedAt()).build();
    }

    public PostStatBO toBO() {
        return PostStatBO.builder().id(id).postId(postId).likeCount(likeCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
