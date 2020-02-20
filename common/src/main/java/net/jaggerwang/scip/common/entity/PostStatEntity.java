package net.jaggerwang.scip.common.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatEntity {
    private Long id;

    private Long postId;

    private Long likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
