package net.jaggerwang.scip.user.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserBindRequestDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserModifyRequestDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserSendEmailVerifyCodeRequestDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserSendMobileVerifyCodeRequestDTO;
import net.jaggerwang.scip.user.usecase.UserUsecase;
import net.jaggerwang.scip.common.adapter.api.controller.BaseController;
import net.jaggerwang.scip.common.usecase.exception.NotFoundException;
import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
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
    public ApiResult<UserDTO> register(@RequestBody UserDTO userDTO) {
        var userBO = userUsecase.register(userDTO.toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @PostMapping("/bind")
    public ApiResult<UserDTO> bind(@RequestBody UserBindRequestDTO requestDTO) {
        var userBO = userUsecase.bind(requestDTO.getExternalAuthProvider(),
                requestDTO.getExternalUserId(), requestDTO.getInternalUser().toBO());

        return new ApiResult(UserDTO.fromBO(userBO));
    }

    @PostMapping("/modify")
    public ApiResult<UserDTO> modify(@RequestBody UserModifyRequestDTO requestDTO) {
        if ((requestDTO.getUser().getMobile() != null
                && !userUsecase.checkMobileVerifyCode("modify", requestDTO.getUser().getMobile(),
                requestDTO.getCode()))
                || requestDTO.getUser().getEmail() != null
                && !userUsecase.checkEmailVerifyCode("modify", requestDTO.getUser().getEmail(),
                requestDTO.getCode())) {
            throw new UsecaseException("验证码错误");
        }

        var userBO = userUsecase.modify(loggedUserId(), requestDTO.getUser().toBO());

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
    public ApiResult<String> sendMobileVerifyCode(
            @RequestBody UserSendMobileVerifyCodeRequestDTO requestDTO) {
        var verifyCode = userUsecase.sendMobileVerifyCode(requestDTO.getType(),
                requestDTO.getMobile());

        return new ApiResult(verifyCode);
    }

    @PostMapping("/sendEmailVerifyCode")
    public ApiResult<String> sendEmailVerifyCode(
            @RequestBody UserSendEmailVerifyCodeRequestDTO requestDTO) {
        var verifyCode = userUsecase.sendEmailVerifyCode(requestDTO.getType(),
                requestDTO.getEmail());

        return new ApiResult(verifyCode);
    }

    @GetMapping("/rolesOfUser")
    public ApiResult<List<RoleDTO>> roles(@RequestParam Long userId) {
        var roleBOs = userUsecase.rolesOfUser(userId);

        return new ApiResult(roleBOs.stream().map(RoleDTO::fromBO).collect(Collectors.toList()));
    }
}
