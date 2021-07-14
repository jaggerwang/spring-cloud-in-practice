package net.jaggerwang.scip.auth.adapter.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.jaggerwang.scip.common.entity.UserBO;
import net.jaggerwang.scip.common.usecase.exception.NotFoundException;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends AbstractController {
    @PostMapping("/register")
    public ApiResult<UserDTO> register(@RequestBody UserDTO userDto) {
        var userBO = authUsecase.register(userDto.toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @GetMapping("/verifyPassword")
    public ApiResult<UserDTO> verifyPassword(@RequestParam(required = false) String username,
                                             @RequestParam(required = false) String mobile,
                                             @RequestParam(required = false) String email,
                                             @RequestParam String password) {
        Optional<UserBO> userBO;
        if (username != null) {
            userBO = authUsecase.infoByUsername(username);
        } else if (mobile != null) {
            userBO = authUsecase.infoByMobile(mobile);
        } else if (email != null) {
            userBO = authUsecase.infoByEmail(email);
        } else {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }

        if (userBO.isEmpty() ||
                !authUsecase.matchPassword(password, userBO.get().getPassword())) {
            throw new UsecaseException("用户名或密码错误");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @PostMapping("/modify")
    public ApiResult<UserDTO> modify(@RequestBody Map<String, Object> input) {
        var userDTO = objectMapper.convertValue(input.get("user"), UserDTO.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDTO.getMobile() != null
                && !authUsecase.checkMobileVerifyCode("modify", userDTO.getMobile(), code))
                || userDTO.getEmail() != null
                && !authUsecase.checkEmailVerifyCode("modify", userDTO.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userBO = authUsecase.modify(loggedUserId(), userDTO.toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @GetMapping("/info")
    public ApiResult<UserDTO> info(@RequestParam Long id) {
        var userBO = authUsecase.info(id);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByUsername")
    public ApiResult<UserDTO> infoByUsername(@RequestParam String username) {
        var userBO = authUsecase.infoByUsername(username);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByMobile")
    public ApiResult<UserDTO> infoByMobile(@RequestParam String mobile) {
        var userBO = authUsecase.infoByMobile(mobile);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByEmail")
    public ApiResult<UserDTO> infoByEmail(@RequestParam String email) {
        var userBO = authUsecase.infoByEmail(email);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @PostMapping("/sendMobileVerifyCode")
    public ApiResult<String> sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = authUsecase.sendMobileVerifyCode(type, mobile);

        return new ApiResult(verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public ApiResult<String> sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = authUsecase.sendEmailVerifyCode(type, email);

        return new ApiResult(verifyCode);
    }

    @GetMapping("/rolesOfUser")
    public ApiResult<List<RoleDTO>> roles(@RequestParam Long userId) {
        var roleBOs = authUsecase.rolesOfUser(userId);

        return new ApiResult(roleBOs.stream().map(RoleDTO::fromBO).collect(Collectors.toList()));
    }
}
