package net.jaggerwang.scip.auth.adapter.api.controller;

import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends AbstractController {
    @PostMapping("/login")
    public RootDTO login(@RequestBody UserDTO userDto) {
        String username = null;
        if (userDto.getUsername() != null)  {
            username = userDto.getUsername();
        } else if (userDto.getMobile() != null) {
            username = userDto.getMobile();
        } else if (userDto.getEmail() != null) {
            username = userDto.getEmail();
        }
        if (StringUtils.isEmpty(username)) {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }
        var password = userDto.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new UsecaseException("密码不能为空");
        }

        var loggedUser = loginUser(username, password);

        var userBO = userService.info(loggedUser.getId());

        return new RootDTO().addDataEntry("user", userBO);
    }

    @GetMapping("/logout")
    public RootDTO logout() {
        var loggedUser = logoutUser();
        if (loggedUser == null) {
            return new RootDTO().addDataEntry("user", null);
        }

        var userBO = userService.info(loggedUser.getId());

        return new RootDTO().addDataEntry("user", userBO);
    }

    @GetMapping("/logged")
    public RootDTO logged() {
        if (loggedUserId() == null) {
            return new RootDTO().addDataEntry("user", null);
        }

        var userBO = userService.info(loggedUserId());

        return new RootDTO().addDataEntry("user", userBO);
    }

    @GetMapping("/rolesOfUser")
    public RootDTO roles(@RequestParam Long userId) {
        var roleBOs = authUsecase.rolesOfUser(userId);

        return new RootDTO().addDataEntry("roles",
                roleBOs.stream().map(RoleDTO::fromBO).collect(Collectors.toList()));
    }
}
