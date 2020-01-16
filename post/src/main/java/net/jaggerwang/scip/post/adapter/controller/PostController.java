package net.jaggerwang.scip.post.adapter.controller;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.post.adapter.controller.dto.PostDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.jaggerwang.scip.post.entity.PostEntity;

@RestController
@RequestMapping("/post")
public class PostController extends AbstractController {
    @PostMapping("/publish")
    public RootDto publish(@RequestBody PostEntity postInput) {
        postInput.setUserId(loggedUserId());
        var postEntity = postUsecases.publish(postInput);

        return new RootDto().addDataEntry("post", PostDto.fromEntity(postEntity));
    }

    @PostMapping("/delete")
    public RootDto delete(@RequestBody Map<String, Object> input) {
        var id = objectMapper.convertValue(input.get("id"), Long.class);

        var postEntity = postUsecases.info(id);
        if (postEntity.isEmpty()) {
            throw new NotFoundException("动态未找到");
        }
        if (!Objects.equals(postEntity.get().getUserId(), loggedUserId())) {
            throw new NotFoundException("无权删除");
        }

        postUsecases.delete(id);

        return new RootDto();
    }

    @GetMapping("/info")
    public RootDto info(@RequestParam Long id) {
        var postEntity = postUsecases.info(id);
        if (postEntity.isEmpty()) {
            throw new NotFoundException("动态未找到");
        }

        return new RootDto().addDataEntry("post", postEntity.map(PostDto::fromEntity).get());
    }

    @GetMapping("/published")
    public RootDto published(@RequestParam(required = false) Long userId,
                             @RequestParam(defaultValue = "10") Long limit,
                             @RequestParam(defaultValue = "0") Long offset) {
        var postEntities = postUsecases.published(userId, limit, offset);

        return new RootDto().addDataEntry("posts", postEntities.stream()
                .map(PostDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/publishedCount")
    public RootDto publishedCount(@RequestParam(required = false) Long userId) {
        var count = postUsecases.publishedCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @PostMapping("/like")
    public RootDto like(@RequestBody Map<String, Object> input) {
        var postId = objectMapper.convertValue(input.get("postId"), Long.class);

        postUsecases.like(loggedUserId(), postId);

        return new RootDto();
    }

    @PostMapping("/unlike")
    public RootDto unlike(@RequestBody Map<String, Object> input) {
        var postId = objectMapper.convertValue(input.get("postId"), Long.class);

        postUsecases.unlike(loggedUserId(), postId);

        return new RootDto();
    }

    @GetMapping("/isLiked")
    public RootDto isLiked(@RequestParam Long postId) {
        var isLiked = postUsecases.isLiked(loggedUserId(), postId);

        return new RootDto().addDataEntry("isLiked", isLiked);
    }

    @GetMapping("/liked")
    public RootDto liked(@RequestParam(required = false) Long userId,
                         @RequestParam(defaultValue = "10") Long limit,
                         @RequestParam(defaultValue = "0") Long offset) {
        var postEntities = postUsecases.liked(userId, limit, offset);

        return new RootDto().addDataEntry("posts", postEntities.stream()
                .map(PostDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/likedCount")
    public RootDto likedCount(@RequestParam(required = false) Long userId) {
        var count = postUsecases.likedCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @GetMapping("/following")
    public RootDto following(@RequestParam(defaultValue = "10") Long limit,
                             @RequestParam Long beforeId,
                             @RequestParam Long afterId) {
        var postEntities = postUsecases.following(loggedUserId(), limit, beforeId, afterId);

        return new RootDto().addDataEntry("posts", postEntities.stream()
                .map(PostDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/followingCount")
    public RootDto followingCount() {
        var count = postUsecases.followingCount(loggedUserId());

        return new RootDto().addDataEntry("count", count);
    }
}
