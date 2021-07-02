package net.jaggerwang.scip.user.adapter.api.controller;

import java.util.Map;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @PostMapping("/follow")
    public RootDTO follow(@RequestBody Map<String, Object> input) {
        var userId = objectMapper.convertValue(input.get("userId"), Long.class);
        userUsecase.follow(loggedUserId(), userId);

        return new RootDTO();
    }

    @PostMapping("/unfollow")
    public RootDTO unfollow(@RequestBody Map<String, Object> input) {
        var userId = objectMapper.convertValue(input.get("userId"), Long.class);
        userUsecase.unfollow(loggedUserId(), userId);

        return new RootDTO();
    }

    @GetMapping("/isFollowing")
    public RootDTO isFollowing(@RequestParam Long userId) {
        var isFollowing = userUsecase.isFollowing(loggedUserId(), userId);

        return new RootDTO().addDataEntry("isFollowing", isFollowing);
    }

    @GetMapping("/following")
    public RootDTO following(@RequestParam(required = false) Long userId,
                             @RequestParam(defaultValue = "20") Long limit,
                             @RequestParam(defaultValue = "0") Long offset) {
        var userIds = userUsecase.following(userId, limit, offset);

        return new RootDTO().addDataEntry("userIds", userIds);
    }

    @GetMapping("/followingCount")
    public RootDTO followingCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followingCount(userId);

        return new RootDTO().addDataEntry("count", count);
    }

    @GetMapping("/follower")
    public RootDTO follower(@RequestParam(required = false) Long userId,
                            @RequestParam(defaultValue = "20") Long limit,
                            @RequestParam(defaultValue = "0") Long offset) {
        var userIds = userUsecase.follower(userId, limit, offset);

        return new RootDTO().addDataEntry("userIds", userIds);
    }

    @GetMapping("/followerCount")
    public RootDTO followerCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followerCount(userId);

        return new RootDTO().addDataEntry("count", count);
    }
}
