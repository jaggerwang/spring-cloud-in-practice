package net.jaggerwang.scip.user.adapter.api.controller;

import java.util.List;
import java.util.Map;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @PostMapping("/follow")
    public ApiResult follow(@RequestBody Map<String, Object> input) {
        var userId = objectMapper.convertValue(input.get("userId"), Long.class);
        userUsecase.follow(loggedUserId(), userId);

        return new ApiResult();
    }

    @PostMapping("/unfollow")
    public ApiResult unfollow(@RequestBody Map<String, Object> input) {
        var userId = objectMapper.convertValue(input.get("userId"), Long.class);
        userUsecase.unfollow(loggedUserId(), userId);

        return new ApiResult();
    }

    @GetMapping("/isFollowing")
    public ApiResult<Boolean> isFollowing(@RequestParam Long userId) {
        var isFollowing = userUsecase.isFollowing(loggedUserId(), userId);

        return new ApiResult(isFollowing);
    }

    @GetMapping("/following")
    public ApiResult<List<Long>> following(@RequestParam(required = false) Long userId,
                                           @RequestParam(defaultValue = "20") Long limit,
                                           @RequestParam(defaultValue = "0") Long offset) {
        var userIds = userUsecase.following(userId, limit, offset);

        return new ApiResult(userIds);
    }

    @GetMapping("/followingCount")
    public ApiResult<Long> followingCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followingCount(userId);

        return new ApiResult(count);
    }

    @GetMapping("/follower")
    public ApiResult<List<Long>> follower(@RequestParam(required = false) Long userId,
                                          @RequestParam(defaultValue = "20") Long limit,
                                          @RequestParam(defaultValue = "0") Long offset) {
        var userIds = userUsecase.follower(userId, limit, offset);

        return new ApiResult(userIds);
    }

    @GetMapping("/followerCount")
    public ApiResult<Long> followerCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followerCount(userId);

        return new ApiResult(count);
    }
}
