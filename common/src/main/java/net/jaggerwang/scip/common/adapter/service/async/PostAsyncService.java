package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.async.PostService;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PostAsyncService extends InternalAsyncService implements PostService {
    protected ObjectMapper objectMapper;

    public PostAsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                            ObjectMapper objectMapper) {
        super(webClient, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<PostDto> publish(PostDto postDto) {
        return postData("/post/publish", postDto)
                .map(data -> objectMapper.convertValue(data.get("post"), PostDto.class));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return postData("/post/delete", Map.of("id", id))
                .map(data -> null);
    }

    @Override
    public Mono<PostDto> info(Long id) {
        return getData("/post/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("post"), PostDto.class));
    }

    @Override
    public Mono<List<PostDto>> published(Long userId, Long limit, Long offset) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (offset != null) {
            params.put("offset", offset.toString());
        }
        return getData("/post/published", params)
                .map(data -> objectMapper.convertValue(data.get("posts"), new TypeReference<>(){}));
    }

    @Override
    public Mono<Long> publishedCount(Long userId) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        return getData("/post/publishedCount", params)
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }

    @Override
    public Mono<Void> like(Long postId) {
        return postData("/post/like", Map.of("postId", postId))
                .map(data -> null);
    }

    @Override
    public Mono<Void> unlike(Long postId) {
        return postData("/post/unlike", Map.of("postId", postId))
                .map(data -> null);
    }

    @Override
    public Mono<Boolean> isLiked(Long postId) {
        return getData("/post/isLiked", Map.of("postId", postId.toString()))
                .map(data -> objectMapper.convertValue(data.get("isLiked"), Boolean.class));
    }

    @Override
    public Mono<List<PostDto>> liked(Long userId, Long limit, Long offset) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (offset != null) {
            params.put("offset", offset.toString());
        }
        return getData("/post/liked", params)
                .map(data -> objectMapper.convertValue(data.get("posts"), new TypeReference<>(){}));
    }

    @Override
    public Mono<Long> likedCount(Long userId) {
        var params = new HashMap<String, String>();
        if (userId != null) {
            params.put("userId", userId.toString());
        }
        return getData("/post/likedCount", params)
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }

    @Override
    public Mono<List<PostDto>> following(Long limit, Long beforeId, Long afterId) {
        var params = new HashMap<String, String>();
        if (limit != null) {
            params.put("limit", limit.toString());
        }
        if (beforeId != null) {
            params.put("beforeId", beforeId.toString());
        }
        if (afterId != null) {
            params.put("afterId", afterId.toString());
        }
        return getData("/post/following", params)
                .map(data -> objectMapper.convertValue(data.get("posts"), new TypeReference<>(){}));
    }

    @Override
    public Mono<Long> followingCount() {
        return getData("/post/followingCount")
                .map(data -> objectMapper.convertValue(data.get("count"), Long.class));
    }
}
