package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.UserStatEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatDto {
    private Long id;

    private Long userId;

    @Builder.Default
    private Long postCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private Long followingCount = 0L;

    @Builder.Default
    private Long followerCount = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserStatDto fromEntity(UserStatEntity userStatEntity) {
        return UserStatDto.builder().id(userStatEntity.getId()).userId(userStatEntity.getUserId())
                .postCount(userStatEntity.getPostCount()).likeCount(userStatEntity.getLikeCount())
                .followingCount(userStatEntity.getFollowingCount())
                .followerCount(userStatEntity.getFollowerCount())
                .createdAt(userStatEntity.getCreatedAt()).updatedAt(userStatEntity.getUpdatedAt())
                .build();
    }

    public UserStatEntity toEntity() {
        return UserStatEntity.builder().id(id).userId(userId).postCount(postCount)
                .likeCount(likeCount).followingCount(followingCount).followerCount(followerCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
