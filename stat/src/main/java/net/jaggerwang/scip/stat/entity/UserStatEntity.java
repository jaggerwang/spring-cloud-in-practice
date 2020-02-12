package net.jaggerwang.scip.stat.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatEntity {
    private Long id;

    private Long userId;

    private Long postCount;

    private Long likeCount;

    private Long followingCount;

    private Long followerCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
