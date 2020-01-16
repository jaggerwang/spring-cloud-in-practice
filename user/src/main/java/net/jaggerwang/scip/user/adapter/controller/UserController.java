package net.jaggerwang.scip.user.adapter.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.adapter.controller.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.user.adapter.controller.dto.UserDto;
import net.jaggerwang.scip.user.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @PostMapping("/register")
    public RootDto register(@RequestBody UserDto userDto) {
        var userEntity = userUsecases.register(userDto.toEntity());

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity));
    }

    @GetMapping("/verifyPassword")
    public RootDto verifyPassword(@RequestParam(required = false) String username,
                                  @RequestParam(required = false) String mobile,
                                  @RequestParam(required = false) String email,
                                  @RequestParam String password) {
        Optional<UserEntity> userEntity;
        if (username != null) {
            userEntity = userUsecases.infoByUsername(username);
        } else if (mobile != null) {
            userEntity = userUsecases.infoByMobile(mobile);
        } else if (email != null) {
            userEntity = userUsecases.infoByEmail(email);
        } else {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }

        if (userEntity.isEmpty() ||
                !userUsecases.matchPassword(password, userEntity.get().getPassword())) {
            throw new UsecaseException("用户名或密码错误");
        }

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity.get()));
    }

    @GetMapping("/logged")
    public RootDto logged() {
        if (loggedUserId() == null) {
            return new RootDto().addDataEntry("user", null);
        }

        var userEntity = userUsecases.info(loggedUserId());
        return new RootDto().addDataEntry("user", userEntity.map(UserDto::fromEntity).get());
    }

    @PostMapping("/modify")
    public RootDto modify(@RequestBody Map<String, Object> input) {
        var userDto = objectMapper.convertValue(input.get("user"), UserDto.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDto.getMobile() != null
                && !userUsecases.checkMobileVerifyCode("modify", userDto.getMobile(), code))
                || userDto.getEmail() != null
                        && !userUsecases.checkEmailVerifyCode("modify", userDto.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userEntity = userUsecases.modify(loggedUserId(), userDto.toEntity());

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity));
    }

    @GetMapping("/info")
    public RootDto info(@RequestParam Long id) {
        var userEntity = userUsecases.info(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity.get()));
    }

    @PostMapping("/follow")
    public RootDto follow(@RequestBody Long userId) {
        userUsecases.follow(loggedUserId(), userId);

        return new RootDto();
    }

    @PostMapping("/unfollow")
    public RootDto unfollow(@RequestBody Long userId) {
        userUsecases.unfollow(loggedUserId(), userId);

        return new RootDto();
    }

    @GetMapping("/isFollowing")
    public RootDto isFollowing(@RequestParam Long userId) {
        var isFollowing = userUsecases.isFollowing(loggedUserId(), userId);

        return new RootDto().addDataEntry("isFollowing", isFollowing);
    }

    @GetMapping("/following")
    public RootDto following(@RequestParam(required = false) Long userId,
                             @RequestParam(defaultValue = "20") Long limit,
                             @RequestParam(defaultValue = "0") Long offset) {
        var userEntities = userUsecases.following(userId, limit, offset);

        return new RootDto().addDataEntry("users", userEntities.stream()
                .map(UserDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/followingCount")
    public RootDto followingCount(@RequestParam(required = false) Long userId) {
        var count = userUsecases.followingCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @GetMapping("/follower")
    public RootDto follower(@RequestParam(required = false) Long userId,
                            @RequestParam(defaultValue = "20") Long limit,
                            @RequestParam(defaultValue = "0") Long offset) {
        var userEntities = userUsecases.follower(userId, limit, offset);

        return new RootDto().addDataEntry("users", userEntities.stream()
                .map(UserDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/followerCount")
    public RootDto followerCount(@RequestParam(required = false) Long userId) {
        var count = userUsecases.followerCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @PostMapping("/sendMobileVerifyCode")
    public RootDto sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = userUsecases.sendMobileVerifyCode(type, mobile);

        return new RootDto().addDataEntry("verifyCode", verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public RootDto sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = userUsecases.sendEmailVerifyCode(type, email);

        return new RootDto().addDataEntry("verifyCode", verifyCode);
    }
}
