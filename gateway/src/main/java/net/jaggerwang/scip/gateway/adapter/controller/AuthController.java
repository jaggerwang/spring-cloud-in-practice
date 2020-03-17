package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.gateway.api.security.LoggedUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserAsyncService userAsyncService;
    private ReactiveAuthenticationManager authManager;

    public AuthController(UserAsyncService userAsyncService, ReactiveAuthenticationManager authManager) {
        this.userAsyncService = userAsyncService;
        this.authManager = authManager;
    }

    @PostMapping("/login")
    public Mono<RootDto> login(@RequestBody UserDto userDto) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(), userDto.getPassword()))
                .flatMap(auth -> ReactiveSecurityContextHolder.getContext()
                        .flatMap(context -> {
                            context.setAuthentication(auth);
                            var loggedUser = (LoggedUser) auth.getPrincipal();
                            return userAsyncService.info(loggedUser.getId());
                        })
                        .map(user -> new RootDto().addDataEntry("user", user))
                        .defaultIfEmpty(new RootDto().addDataEntry("user", null)));
    }

    @GetMapping("/logout")
    public Mono<RootDto> logout() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.just(new RootDto().addDataEntry("user", null));
                    }

                    var loggedUser = (LoggedUser) auth.getPrincipal();
                    context.setAuthentication(null);
                    return userAsyncService.info(loggedUser.getId())
                            .map(user -> new RootDto().addDataEntry("user", user));
                })
                .defaultIfEmpty(new RootDto().addDataEntry("user", null));
    }

    @GetMapping("/logged")
    public Mono<RootDto> logged() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.just(new RootDto().addDataEntry("user", null));
                    }

                    var loggedUser = (LoggedUser) auth.getPrincipal();
                    return userAsyncService.info(loggedUser.getId())
                            .map(user -> new RootDto().addDataEntry("user", user));
                })
                .defaultIfEmpty(new RootDto().addDataEntry("user", null));
    }
}
