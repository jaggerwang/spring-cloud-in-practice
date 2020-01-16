package net.jaggerwang.scip.stat.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatEntity {
    @NonNull
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private Long postCount;

    @NonNull
    private Long likeCount;

    @NonNull
    private Long followingCount;

    @NonNull
    private Long followerCount;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
