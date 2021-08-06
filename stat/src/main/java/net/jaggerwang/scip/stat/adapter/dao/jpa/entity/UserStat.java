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
import net.jaggerwang.scip.common.entity.UserStatBO;

@Entity
@Table(name = "user_stat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_count")
    private Long postCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "following_count")
    private Long followingCount;

    @Column(name = "follower_count")
    private Long followerCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static UserStat fromBO(UserStatBO fileEntity) {
        return UserStat.builder().id(fileEntity.getId()).userId(fileEntity.getUserId())
                .postCount(fileEntity.getPostCount()).likeCount(fileEntity.getLikeCount())
                .followingCount(fileEntity.getFollowingCount())
                .followerCount(fileEntity.getFollowerCount()).createdAt(fileEntity.getCreatedAt())
                .updatedAt(fileEntity.getUpdatedAt()).build();
    }

    public UserStatBO toBO() {
        return UserStatBO.builder().id(id).userId(userId).postCount(postCount)
                .likeCount(likeCount).followingCount(followingCount).followerCount(followerCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
