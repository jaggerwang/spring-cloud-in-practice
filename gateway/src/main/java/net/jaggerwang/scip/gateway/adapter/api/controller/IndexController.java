package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public Mono<ApiResult<Authentication>> index() {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.just(new SecurityContextImpl()))
                .map(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    return new ApiResult<>(auth);
                });
    }
}
