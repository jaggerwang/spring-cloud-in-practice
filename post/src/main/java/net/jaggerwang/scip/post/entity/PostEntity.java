package net.jaggerwang.scip.post.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    public enum Type {
        TEXT, IMAGE, VIDEO
    }

    @NonNull
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private Type type;

    @NonNull
    private String text;

    @NonNull
    private List<Long> imageIds;

    private Long videoId;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
