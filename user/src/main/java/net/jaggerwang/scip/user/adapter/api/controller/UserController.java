package net.jaggerwang.scip.user.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.user.usecase.UserUsecase;
import net.jaggerwang.scip.common.adapter.api.controller.BaseController;
import net.jaggerwang.scip.common.entity.UserBO;
import net.jaggerwang.scip.common.usecase.exception.NotFoundException;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private UserUsecase userUsecase;

    public UserController(HttpServletRequest request, ObjectMapper objectMapper,
                          UserUsecase userUsecase) {
        super(request, objectMapper);

        this.userUsecase = userUsecase;
    }

    @PostMapping("/register")
    public ApiResult<UserDTO> register(@RequestBody UserDTO userDto) {
        var userBO = userUsecase.register(userDto.toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @PostMapping("/modify")
    public ApiResult<UserDTO> modify(@RequestBody Map<String, Object> input) {
        var userDTO = objectMapper.convertValue(input.get("user"), UserDTO.class);
        var code = objectMapper.convertValue(input.get("code"), String.class);

        if ((userDTO.getMobile() != null
                && !userUsecase.checkMobileVerifyCode("modify", userDTO.getMobile(), code))
                || userDTO.getEmail() != null
                && !userUsecase.checkEmailVerifyCode("modify", userDTO.getEmail(), code)) {
            throw new UsecaseException("验证码错误");
        }

        var userBO = userUsecase.modify(loggedUserId(), userDTO.toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @GetMapping("/info")
    public ApiResult<UserDTO> info(@RequestParam Long id) {
        var userBO = userUsecase.info(id);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByUsername")
    public ApiResult<UserDTO> infoByUsername(@RequestParam String username) {
        var userBO = userUsecase.infoByUsername(username);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByMobile")
    public ApiResult<UserDTO> infoByMobile(@RequestParam String mobile) {
        var userBO = userUsecase.infoByMobile(mobile);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @GetMapping("/infoByEmail")
    public ApiResult<UserDTO> infoByEmail(@RequestParam String email) {
        var userBO = userUsecase.infoByEmail(email);
        if (userBO.isEmpty()) {
            throw new NotFoundException("用户未找到");
        }

        return new ApiResult(UserDTO.fromBO(userBO.get()));
    }

    @PostMapping("/sendMobileVerifyCode")
    public ApiResult<String> sendMobileVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var mobile = objectMapper.convertValue(input.get("mobile"), String.class);

        var verifyCode = userUsecase.sendMobileVerifyCode(type, mobile);

        return new ApiResult(verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public ApiResult<String> sendEmailVerifyCode(@RequestBody Map<String, Object> input) {
        var type = objectMapper.convertValue(input.get("type"), String.class);
        var email = objectMapper.convertValue(input.get("email"), String.class);

        var verifyCode = userUsecase.sendEmailVerifyCode(type, email);

        return new ApiResult(verifyCode);
    }

    @GetMapping("/rolesOfUser")
    public ApiResult<List<RoleDTO>> roles(@RequestParam Long userId) {
        var roleBOs = userUsecase.rolesOfUser(userId);

        return new ApiResult(roleBOs.stream().map(RoleDTO::fromBO).collect(Collectors.toList()));
    }
}
