package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.UserStatBO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatDTO {
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

    public static UserStatDTO fromEntity(UserStatBO userStatBO) {
        return UserStatDTO.builder().id(userStatBO.getId()).userId(userStatBO.getUserId())
                .postCount(userStatBO.getPostCount()).likeCount(userStatBO.getLikeCount())
                .followingCount(userStatBO.getFollowingCount())
                .followerCount(userStatBO.getFollowerCount())
                .createdAt(userStatBO.getCreatedAt()).updatedAt(userStatBO.getUpdatedAt())
                .build();
    }

    public UserStatBO toEntity() {
        return UserStatBO.builder().id(id).userId(userId).postCount(postCount)
                .likeCount(likeCount).followingCount(followingCount).followerCount(followerCount)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
