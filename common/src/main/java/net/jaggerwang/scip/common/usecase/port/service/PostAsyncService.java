package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostAsyncService {
    Mono<PostDTO> publish(PostDTO postDto);

    Mono<Void> delete(Long id);

    Mono<PostDTO> info(Long id);

    Mono<List<PostDTO>> published(Long userId, Long limit, Long offset);

    Mono<Long> publishedCount(Long userId);

    Mono<Void> like(Long postId);

    Mono<Void> unlike(Long postId);

    Mono<Boolean> isLiked(Long postId);

    Mono<List<PostDTO>> liked(Long userId, Long limit, Long offset);

    Mono<Long> likedCount(Long userId);

    Mono<List<PostDTO>> following(Long limit, Long beforeId, Long afterId);

    Mono<Long> followingCount();
}
