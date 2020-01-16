package net.jaggerwang.scip.stat.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatEntity {
    @NonNull
    private Long id;

    @NonNull
    private Long postId;

    @NonNull
    private Long likeCount;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
