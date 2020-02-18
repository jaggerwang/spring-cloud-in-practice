package net.jaggerwang.scip.user.adapter.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.user.adapter.controller.dto.UserDto;
import net.jaggerwang.scip.user.entity.UserEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @PostMapping("/register")
    public RootDto register(@RequestBody UserDto userDto) {
        var userEntity = userUsecase.register(userDto.toEntity());

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity));
    }

    @GetMapping("/verifyPassword")
    public RootDto verifyPassword(@RequestParam(required = false) String username,
                                  @RequestParam(required = false) String mobile,
                                  @RequestParam(required = false) String email,
                                  @RequestParam String password) {
        Optional<UserEntity> userEntity;
        if (username != null) {
            userEntity = userUsecase.infoByUsername(username);
        } else if (mobile != null) {
            userEntity = userUsecase.infoByMobile(mobile);
        } else if (email != null) {
            userEntity = userUsecase.infoByEmail(email);
        } else {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }

        if (userEntity.isEmpty() ||
                !userUsecase.matchPassword(password, userEntity.get().getPassword())) {
            throw new UsecaseException("用户名或密码错误");
        }

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity.get()));
    }

    @GetMapping("/logged")
    public RootDto logged() {
        if (loggedUserId() == null) {
            return new RootDto().addDataEntry("user", null);
        }

        var userEntity = userUsecase.info(loggedUserId());
        return new RootDto().addDataEntry("user", userEntity.map(UserDto::fromEntity).get());
    }

    @PostMapping("/modify")
    public RootDto modify(@RequestBody Map<String, Object> input) {
        var userDto = objectMapper.convertValue(input.get("user"), UserDto.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDto.getMobile() != null
                && !userUsecase.checkMobileVerifyCode("modify", userDto.getMobile(), code))
                || userDto.getEmail() != null
                        && !userUsecase.checkEmailVerifyCode("modify", userDto.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userEntity = userUsecase.modify(loggedUserId(), userDto.toEntity());

        return new RootDto().addDataEntry("user", UserDto.fromEntity(userEntity));
    }

    @GetMapping("/info")
    public RootDto info(@RequestParam Long id, @RequestParam(defaultValue = "false") Boolean full) {
        var userEntity = userUsecase.info(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDto().addDataEntry("user",
                full ? fullUserDto(userEntity.get()) : UserDto.fromEntity(userEntity.get()));
    }

    @PostMapping("/follow")
    public RootDto follow(@RequestBody Long userId) {
        userUsecase.follow(loggedUserId(), userId);

        return new RootDto();
    }

    @PostMapping("/unfollow")
    public RootDto unfollow(@RequestBody Long userId) {
        userUsecase.unfollow(loggedUserId(), userId);

        return new RootDto();
    }

    @GetMapping("/isFollowing")
    public RootDto isFollowing(@RequestParam Long userId) {
        var isFollowing = userUsecase.isFollowing(loggedUserId(), userId);

        return new RootDto().addDataEntry("isFollowing", isFollowing);
    }

    @GetMapping("/following")
    public RootDto following(@RequestParam(required = false) Long userId,
                             @RequestParam(defaultValue = "20") Long limit,
                             @RequestParam(defaultValue = "0") Long offset) {
        var userEntities = userUsecase.following(userId, limit, offset);

        return new RootDto().addDataEntry("users", userEntities.stream()
                .map(UserDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/followingCount")
    public RootDto followingCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followingCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @GetMapping("/follower")
    public RootDto follower(@RequestParam(required = false) Long userId,
                            @RequestParam(defaultValue = "20") Long limit,
                            @RequestParam(defaultValue = "0") Long offset) {
        var userEntities = userUsecase.follower(userId, limit, offset);

        return new RootDto().addDataEntry("users", userEntities.stream()
                .map(UserDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/followerCount")
    public RootDto followerCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followerCount(userId);

        return new RootDto().addDataEntry("count", count);
    }

    @PostMapping("/sendMobileVerifyCode")
    public RootDto sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = userUsecase.sendMobileVerifyCode(type, mobile);

        return new RootDto().addDataEntry("verifyCode", verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public RootDto sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = userUsecase.sendEmailVerifyCode(type, email);

        return new RootDto().addDataEntry("verifyCode", verifyCode);
    }
}
