package net.jaggerwang.scip.gateway.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public Mono<RootDTO> index() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    var auth = securityContext.getAuthentication();
                    return new RootDTO().addDataEntry("authentication", auth);
                });
    }
}
