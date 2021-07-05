package net.jaggerwang.scip.user.adapter.api.controller;

import java.util.Map;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserBO;
import net.jaggerwang.scip.common.usecase.exception.NotFoundException;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @PostMapping("/register")
    public RootDTO register(@RequestBody UserDTO userDto) {
        var userEntity = userUsecase.register(userDto.toEntity());

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity));
    }

    @GetMapping("/verifyPassword")
    public RootDTO verifyPassword(@RequestParam(required = false) String username,
                                  @RequestParam(required = false) String mobile,
                                  @RequestParam(required = false) String email,
                                  @RequestParam String password) {
        Optional<UserBO> userEntity;
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

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity.get()));
    }

    @PostMapping("/modify")
    public RootDTO modify(@RequestBody Map<String, Object> input) {
        var userDto = objectMapper.convertValue(input.get("user"), UserDTO.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDto.getMobile() != null
                && !userUsecase.checkMobileVerifyCode("modify", userDto.getMobile(), code))
                || userDto.getEmail() != null
                && !userUsecase.checkEmailVerifyCode("modify", userDto.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userEntity = userUsecase.modify(loggedUserId(), userDto.toEntity());

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity));
    }

    @GetMapping("/info")
    public RootDTO info(@RequestParam Long id,
                        @RequestParam(defaultValue = "false") Boolean full) {
        var userEntity = userUsecase.info(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                full ? fullUserDto(userEntity.get()) : UserDTO.fromEntity(userEntity.get()));
    }

    @GetMapping("/infoByUsername")
    public RootDTO infoByUsername(@RequestParam String username,
                                  @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = userUsecase.infoByUsername(username);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @GetMapping("/infoByMobile")
    public RootDTO infoByMobile(@RequestParam String mobile,
                                @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = userUsecase.infoByMobile(mobile);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @GetMapping("/infoByEmail")
    public RootDTO infoByEmail(@RequestParam String email,
                               @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = userUsecase.infoByEmail(email);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @PostMapping("/sendMobileVerifyCode")
    public RootDTO sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = userUsecase.sendMobileVerifyCode(type, mobile);

        return new RootDTO().addDataEntry("verifyCode", verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public RootDTO sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = userUsecase.sendEmailVerifyCode(type, email);

        return new RootDTO().addDataEntry("verifyCode", verifyCode);
    }

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
        var users = userUsecase.following(userId, limit, offset);

        return new RootDTO().addDataEntry("users", users);
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
        var users = userUsecase.follower(userId, limit, offset);

        return new RootDTO().addDataEntry("users", users);
    }

    @GetMapping("/followerCount")
    public RootDTO followerCount(@RequestParam(required = false) Long userId) {
        var count = userUsecase.followerCount(userId);

        return new RootDTO().addDataEntry("count", count);
    }
}
