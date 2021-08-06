package net.jaggerwang.scip.common.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;


/**
 * @author Jagger Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostBO {
    public enum Type {
        TEXT, IMAGE, VIDEO
    }

    private Long id;

    private Long userId;

    private Type type;

    private String text;

    private List<Long> imageIds;

    private Long videoId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
