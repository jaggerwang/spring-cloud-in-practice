package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.gateway.usecase.port.service.ReactiveAuthService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/")
public class IndexController extends AbstractController {
    private ReactiveAuthService reactiveAuthService;

    public IndexController(ReactiveAuthService reactiveAuthService) {
        this.reactiveAuthService = reactiveAuthService;
    }

    @PostMapping("/login")
    public Mono<ApiResult<UserDTO>> login(ServerWebExchange exchange,
                                          @RequestBody UserDTO userDto) {
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

        return loginUser(exchange, username, password)
                .flatMap(loggedUser -> reactiveAuthService.info(loggedUser.getId()))
                .map(apiResult -> new ApiResult<>(apiResult.getData()));
    }

    @GetMapping("/logout")
    public Mono<Void> logout(ServerWebExchange exchange) {
        return logoutUser(exchange);
    }

    @GetMapping("/logged")
    public Mono<ApiResult<UserDTO>> logged() {
        return loggedUserId()
                .flatMap(userId -> reactiveAuthService.info(userId))
                .map(apiResult -> new ApiResult<>(apiResult.getData()))
                .defaultIfEmpty(new ApiResult<>());
    }
}
