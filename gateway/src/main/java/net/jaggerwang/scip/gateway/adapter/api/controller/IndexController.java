package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class IndexController extends AbstractController {
    @GetMapping("/")
    public Mono<RootDto> index() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    return new RootDto().addDataEntry("authentication", auth);
                });
    }
}
