package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private Long id;

    private Long userId;

    private String region;

    private String bucket;

    private String path;

    private Map<String, Object> meta;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
