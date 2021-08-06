package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.exception.UsecaseException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.gateway.usecase.port.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Mono<ApiResult<UserDTO>> login(ServerWebExchange exchange,
                                          @RequestBody UserDTO userDTO) {
        String username = null;
        if (userDTO.getUsername() != null)  {
            username = userDTO.getUsername();
        } else if (userDTO.getMobile() != null) {
            username = userDTO.getMobile();
        } else if (userDTO.getEmail() != null) {
            username = userDTO.getEmail();
        }
        if (!StringUtils.hasText(username)) {
            throw new UsecaseException("用户名、手机或邮箱不能都为空");
        }
        var password = userDTO.getPassword();
        if (!StringUtils.hasText(password)) {
            throw new UsecaseException("密码不能为空");
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(auth -> ReactiveSecurityContextHolder.getContext()
                        .defaultIfEmpty(new SecurityContextImpl())
                        .flatMap(securityContext -> exchange.getSession()
                                .map(session -> {
                                    securityContext.setAuthentication(auth);
                                    session.getAttributes().put(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME,
                                            securityContext);
                                    return (LoggedUser) auth.getPrincipal();
                                })))
                .flatMap(loggedUser -> userService.userInfo(loggedUser.getId()));
    }

    @GetMapping("/logout")
    public Mono<ApiResult<UserDTO>> logout(ServerWebExchange exchange) {
        return ReactiveSecurityContextHolder.getContext()
                .defaultIfEmpty(new SecurityContextImpl())
                .flatMap(securityContext -> exchange.getSession()
                        .flatMap(session -> {
                            var auth = securityContext.getAuthentication();
                            securityContext.setAuthentication(null);
                            session.getAttributes().remove(
                                    DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);

                            if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                                    !auth.isAuthenticated()) {
                                return Mono.empty();
                            }
                            return Mono.just((LoggedUser) auth.getPrincipal());
                        }))
                .flatMap(loggedUser -> userService.userInfo(loggedUser.getId()))
                .defaultIfEmpty(new ApiResult<>());
    }

    @GetMapping("/logged")
    public Mono<ApiResult<UserDTO>> logged() {
        return loggedUser()
                .flatMap(loggedUser -> userService.userInfo(loggedUser.getId()))
                .defaultIfEmpty(new ApiResult<>());
    }
}
