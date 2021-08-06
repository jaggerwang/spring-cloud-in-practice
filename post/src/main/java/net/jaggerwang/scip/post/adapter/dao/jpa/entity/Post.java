package net.jaggerwang.scip.post.adapter.dao.jpa.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.jaggerwang.scip.post.adapter.dao.jpa.converter.PostImageIdsConverter;
import net.jaggerwang.scip.common.entity.PostBO;

/**
 * @author Jagger Wang
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private PostBO.Type type;

    private String text;

    @Column(name = "image_ids")
    @Convert(converter = PostImageIdsConverter.class)
    private List<Long> imageIds;

    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Post fromBO(PostBO userEntity) {
        return Post.builder().id(userEntity.getId()).userId(userEntity.getUserId())
                .type(userEntity.getType()).text(userEntity.getText())
                .imageIds(userEntity.getImageIds()).videoId(userEntity.getVideoId())
                .createdAt(userEntity.getCreatedAt()).updatedAt(userEntity.getUpdatedAt()).build();
    }

    public PostBO toBO() {
        return PostBO.builder().id(id).userId(userId).type(type).text(text).imageIds(imageIds)
                .videoId(videoId).createdAt(createdAt).updatedAt(updatedAt).build();
    }

    @PrePersist
    public void prePersist() {
        if (text == null)
            text = "";
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
