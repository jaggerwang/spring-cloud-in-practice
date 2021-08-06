package net.jaggerwang.scip.user.adapter.api.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.api.controller.BaseController;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserFollowRequestDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserUnfollowRequestDTO;
import net.jaggerwang.scip.user.usecase.FollowUsecase;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/follow")
public class FollowController extends BaseController {
    protected FollowUsecase followUsecase;

    public FollowController(HttpServletRequest request, ObjectMapper objectMapper,
                            FollowUsecase followUsecase) {
        super(request, objectMapper);

        this.followUsecase = followUsecase;
    }

    @PostMapping("/follow")
    public ApiResult follow(@RequestBody UserFollowRequestDTO requestDTO) {
        followUsecase.follow(loggedUserId(), requestDTO.getUserId());

        return new ApiResult();
    }

    @PostMapping("/unfollow")
    public ApiResult unfollow(@RequestBody UserUnfollowRequestDTO requestDTO) {
        followUsecase.unfollow(loggedUserId(), requestDTO.getUserId());

        return new ApiResult();
    }

    @GetMapping("/isFollowing")
    public ApiResult<Boolean> isFollowing(@RequestParam Long userId) {
        var isFollowing = followUsecase.isFollowing(loggedUserId(), userId);

        return new ApiResult(isFollowing);
    }

    @GetMapping("/following")
    public ApiResult<List<Long>> following(@RequestParam(required = false) Long userId,
                                           @RequestParam(defaultValue = "20") Long limit,
                                           @RequestParam(defaultValue = "0") Long offset) {
        var userIds = followUsecase.following(userId, limit, offset);

        return new ApiResult(userIds);
    }

    @GetMapping("/followingCount")
    public ApiResult<Long> followingCount(@RequestParam(required = false) Long userId) {
        var count = followUsecase.followingCount(userId);

        return new ApiResult(count);
    }

    @GetMapping("/follower")
    public ApiResult<List<Long>> follower(@RequestParam(required = false) Long userId,
                                          @RequestParam(defaultValue = "20") Long limit,
                                          @RequestParam(defaultValue = "0") Long offset) {
        var userIds = followUsecase.follower(userId, limit, offset);

        return new ApiResult(userIds);
    }

    @GetMapping("/followerCount")
    public ApiResult<Long> followerCount(@RequestParam(required = false) Long userId) {
        var count = followUsecase.followerCount(userId);

        return new ApiResult(count);
    }
}
