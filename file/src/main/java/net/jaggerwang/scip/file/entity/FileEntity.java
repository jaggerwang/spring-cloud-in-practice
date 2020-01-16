package net.jaggerwang.scip.file.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
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

    @NonNull
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private Region region;

    @NonNull
    private String bucket;

    @NonNull
    private String path;

    @NonNull
    private Meta meta;

    @NonNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
