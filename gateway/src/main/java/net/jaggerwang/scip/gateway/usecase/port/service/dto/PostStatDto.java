package net.jaggerwang.scip.gateway.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostStatDto {
    private Long id;

    private Long postId;

    private Long likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
