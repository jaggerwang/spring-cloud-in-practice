package net.jaggerwang.scip.auth.adapter.api.controller;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.entity.UserBO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController extends AbstractController {
    @PostMapping("/register")
    public RootDTO register(@RequestBody UserDTO userDto) {
        var userEntity = authUsecase.register(userDto.toEntity());

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity));
    }

    @GetMapping("/verifyPassword")
    public RootDTO verifyPassword(@RequestParam(required = false) String username,
                                  @RequestParam(required = false) String mobile,
                                  @RequestParam(required = false) String email,
                                  @RequestParam String password) {
        Optional<UserBO> userEntity;
        if (username != null) {
            userEntity = authUsecase.infoByUsername(username);
        } else if (mobile != null) {
            userEntity = authUsecase.infoByMobile(mobile);
        } else if (email != null) {
            userEntity = authUsecase.infoByEmail(email);
        } else {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }

        if (userEntity.isEmpty() ||
                !authUsecase.matchPassword(password, userEntity.get().getPassword())) {
            throw new UsecaseException("用户名或密码错误");
        }

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity.get()));
    }

    @PostMapping("/modify")
    public RootDTO modify(@RequestBody Map<String, Object> input) {
        var userDto = objectMapper.convertValue(input.get("user"), UserDTO.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDto.getMobile() != null
                && !authUsecase.checkMobileVerifyCode("modify", userDto.getMobile(), code))
                || userDto.getEmail() != null
                        && !authUsecase.checkEmailVerifyCode("modify", userDto.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userEntity = authUsecase.modify(loggedUserId(), userDto.toEntity());

        return new RootDTO().addDataEntry("user", UserDTO.fromEntity(userEntity));
    }

    @GetMapping("/info")
    public RootDTO info(@RequestParam Long id,
                        @RequestParam(defaultValue = "false") Boolean full) {
        var userEntity = authUsecase.info(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                full ? fullUserDto(userEntity.get()) : UserDTO.fromEntity(userEntity.get()));
    }

    @GetMapping("/infoByUsername")
    public RootDTO infoByUsername(@RequestParam String username,
                                  @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = authUsecase.infoByUsername(username);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @GetMapping("/infoByMobile")
    public RootDTO infoByMobile(@RequestParam String mobile,
                                @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = authUsecase.infoByMobile(mobile);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @GetMapping("/infoByEmail")
    public RootDTO infoByEmail(@RequestParam String email,
                               @RequestParam(defaultValue = "false") Boolean withPassword) {
        var userEntity = authUsecase.infoByEmail(email);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new RootDTO().addDataEntry("user",
                UserDTO.fromEntity(userEntity.get(), withPassword));
    }

    @GetMapping("/roles")
    public RootDTO roles(@RequestParam String username) {
        var roleEntities = authUsecase.roles(username);

        return new RootDTO().addDataEntry("roles",
                roleEntities.stream().map(RoleDTO::fromEntity).collect(Collectors.toList()));
    }

    @PostMapping("/sendMobileVerifyCode")
    public RootDTO sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = authUsecase.sendMobileVerifyCode(type, mobile);

        return new RootDTO().addDataEntry("verifyCode", verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public RootDTO sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = authUsecase.sendEmailVerifyCode(type, email);

        return new RootDTO().addDataEntry("verifyCode", verifyCode);
    }
}
