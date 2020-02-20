package net.jaggerwang.scip.common.usecase.port.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.common.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;

    private Long userId;

    private PostEntity.Type type;

    private String text;

    private List<Long> imageIds;

    private Long videoId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostDto fromEntity(PostEntity postEntity) {
        return PostDto.builder().id(postEntity.getId()).userId(postEntity.getUserId())
                .type(postEntity.getType()).text(postEntity.getText())
                .imageIds(postEntity.getImageIds()).videoId(postEntity.getVideoId())
                .createdAt(postEntity.getCreatedAt()).updatedAt(postEntity.getUpdatedAt()).build();
    }

    public PostEntity toEntity() {
        return PostEntity.builder().id(id).userId(userId).type(type).text(text).imageIds(imageIds)
                .videoId(videoId).createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
