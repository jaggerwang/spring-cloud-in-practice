package net.jaggerwang.scip.common.usecase.port.service.async;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostAsyncService {
    Mono<PostDto> publish(PostDto postDto);

    Mono<Void> delete(Long id);

    Mono<PostDto> info(Long id);

    Mono<List<PostDto>> published(Long userId, Long limit, Long offset);

    Mono<Long> publishedCount(Long userId);

    Mono<Void> like(Long postId);

    Mono<Void> unlike(Long postId);

    Mono<Boolean> isLiked(Long postId);

    Mono<List<PostDto>> liked(Long userId, Long limit, Long offset);

    Mono<Long> likedCount(Long userId);

    Mono<List<PostDto>> following(Long limit, Long beforeId, Long afterId);

    Mono<Long> followingCount();
}
