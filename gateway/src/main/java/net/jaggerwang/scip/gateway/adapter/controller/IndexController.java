package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class IndexController extends AbstractController {
    @GetMapping("/")
    public Mono<RootDto> index() {
        return getSecurityContext()
                .map(context -> {
                    var auth = context.getAuthentication();
                    return new RootDto().addDataEntry("authentication", auth);
                });
    }
}
