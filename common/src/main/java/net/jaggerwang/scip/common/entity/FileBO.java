package net.jaggerwang.scip.common.entity;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author Jagger Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileBO {
    public static enum Region {
        LOCAL
    }

    public static enum ThumbType {
        SMALL, MIDDLE, LARGE, HUGE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String name;

        private Long size;

        private String type;
    }

    private Long id;

    private Long userId;

    private Region region;

    private String bucket;

    private String path;

    private Meta meta;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
