package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public Mono<RootDto> index() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> new RootDto().addDataEntry("authentication", authentication));
    }
}
