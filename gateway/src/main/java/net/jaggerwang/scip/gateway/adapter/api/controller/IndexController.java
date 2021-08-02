package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/")
public class IndexController extends BaseController {
    @GetMapping("/")
    public Mono<ApiResult<LoggedUser>> logged() {
        return loggedUser()
                .map(loggedUser -> new ApiResult<>(loggedUser))
                .defaultIfEmpty(new ApiResult<>());
    }
}
