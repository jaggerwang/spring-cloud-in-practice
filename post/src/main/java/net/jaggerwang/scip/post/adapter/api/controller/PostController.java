package net.jaggerwang.scip.post.adapter.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostDTO;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.jaggerwang.scip.common.entity.PostBO;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/post")
public class PostController extends AbstractController {
    @PostMapping("/publish")
    public ApiResult<PostDTO> publish(@RequestBody PostBO postInput) {
        postInput.setUserId(loggedUserId());
        var postBO = postUsecase.publish(postInput);

        return new ApiResult(PostDTO.fromBO(postBO));
    }

    @PostMapping("/delete")
    public ApiResult delete(@RequestBody Map<String, Object> input) {
        var id = objectMapper.convertValue(input.get("id"), Long.class);

        var postBO = postUsecase.info(id);
        if (postBO.isEmpty()) {
            throw new NotFoundException("动态未找到");
        }
        if (!Objects.equals(postBO.get().getUserId(), loggedUserId())) {
            throw new NotFoundException("无权删除");
        }

        postUsecase.delete(id);

        return new ApiResult();
    }

    @GetMapping("/info")
    public ApiResult<PostDTO> info(@RequestParam Long id) {
        var postBO = postUsecase.info(id);
        if (postBO.isEmpty()) {
            throw new NotFoundException("动态未找到");
        }

        return new ApiResult(postBO.map(PostDTO::fromBO).get());
    }

    @GetMapping("/published")
    public ApiResult<List<PostDTO>> published(@RequestParam(required = false) Long userId,
                                              @RequestParam(defaultValue = "10") Long limit,
                                              @RequestParam(defaultValue = "0") Long offset) {
        var postBOs = postUsecase.published(userId, limit, offset);

        return new ApiResult(postBOs.stream().map(PostDTO::fromBO).collect(Collectors.toList()));
    }

    @GetMapping("/publishedCount")
    public ApiResult<Long> publishedCount(@RequestParam(required = false) Long userId) {
        var count = postUsecase.publishedCount(userId);

        return new ApiResult(count);
    }

    @PostMapping("/like")
    public ApiResult like(@RequestBody Map<String, Object> input) {
        var postId = objectMapper.convertValue(input.get("postId"), Long.class);

        postUsecase.like(loggedUserId(), postId);

        return new ApiResult();
    }

    @PostMapping("/unlike")
    public ApiResult unlike(@RequestBody Map<String, Object> input) {
        var postId = objectMapper.convertValue(input.get("postId"), Long.class);

        postUsecase.unlike(loggedUserId(), postId);

        return new ApiResult();
    }

    @GetMapping("/isLiked")
    public ApiResult<Boolean> isLiked(@RequestParam Long postId) {
        var isLiked = postUsecase.isLiked(loggedUserId(), postId);

        return new ApiResult(isLiked);
    }

    @GetMapping("/liked")
    public ApiResult<List<PostDTO>> liked(@RequestParam(required = false) Long userId,
                                          @RequestParam(defaultValue = "10") Long limit,
                                          @RequestParam(defaultValue = "0") Long offset) {
        var postBOs = postUsecase.liked(userId, limit, offset);

        return new ApiResult(postBOs.stream().map(PostDTO::fromBO).collect(Collectors.toList()));
    }

    @GetMapping("/likedCount")
    public ApiResult<Long> likedCount(@RequestParam(required = false) Long userId) {
        var count = postUsecase.likedCount(userId);

        return new ApiResult(count);
    }

    @GetMapping("/following")
    public ApiResult<List<PostDTO>> following(@RequestParam(defaultValue = "10") Long limit,
                                              @RequestParam(required = false) Long beforeId,
                                              @RequestParam(required = false) Long afterId) {
        var postBOs = postUsecase.following(loggedUserId(), limit, beforeId, afterId);

        return new ApiResult(postBOs.stream().map(PostDTO::fromBO).collect(Collectors.toList()));
    }

    @GetMapping("/followingCount")
    public ApiResult<Long> followingCount() {
        var count = postUsecase.followingCount(loggedUserId());

        return new ApiResult(count);
    }
}
