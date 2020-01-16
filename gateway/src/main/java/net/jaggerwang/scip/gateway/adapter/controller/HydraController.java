package net.jaggerwang.scip.gateway.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
@RequestMapping("/hydra")
public class HydraController extends AbstractController {
    @GetMapping("/login")
    public Mono<String> login(@RequestParam(name = "login_challenge") String challenge,
                              Model model) {
        return hydraService
                .getLoginRequest(challenge)
                .flatMap(loginRequest -> {
                    if (loginRequest.getSkip()) {
                        var loginAccept = LoginAcceptDto.builder()
                                .subject(loginRequest.getSubject())
                                .build();
                        return hydraService
                                .directlyAcceptLoginRequest(challenge, loginAccept)
                                .map(redirectTo -> "redirect:"+redirectTo);
                    }

                    model.addAttribute("challenge", challenge);
                    return Mono.just("hydra/login");
        });
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestParam String challenge,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String mobile,
                              @RequestParam(required = false) String email,
                              @RequestParam String password,
                              @RequestParam(required = false, defaultValue = "0") Boolean remember,
                              @RequestParam String submit,
                              Model model) {
        if (submit.equals("No")) {
            var loginReject = LoginRejectDto.builder()
                    .error("login_rejected")
                    .errorDescription("The resource owner rejected to log in")
                    .build();
            return hydraService
                    .rejectLoginRequest(challenge, loginReject)
                    .map(redirectTo -> "redirect:" + redirectTo);
        }

        Mono<UserDto> mono;
        if (username != null) {
            mono = userService.verifyPasswordByUsername(username, password);
        } else if (mobile != null) {
            mono = userService.verifyPasswordByMobile(mobile, password);
        } else if (email != null) {
            mono = userService.verifyPasswordByEmail(email, password);
        } else {
            model.addAttribute("error", "用户名、手机或邮箱不能都为空");
            return Mono.just("hydra/login");
        }

        return mono
                .flatMap(userDto -> {
                    var loginAccept = LoginAcceptDto.builder()
                            .subject(userDto.getId().toString())
                            .remember(remember)
                            .rememberFor(86400)
                            .build();
                    return hydraService
                            .acceptLoginRequest(challenge, loginAccept)
                            .map(redirectTo -> "redirect:"+redirectTo);
                });
    }

    @GetMapping("/consent")
    public Mono<String> consent(@RequestParam(name = "consent_challenge") String challenge,
                                Model model) {
        return hydraService
                .getConsentRequest(challenge)
                .flatMap(consentRequest -> {
                    if (consentRequest.getSkip()) {
                        var consentAccept = ConsentAcceptDto.builder()
                                .grantScope(consentRequest.getRequestedScope())
                                .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                                .build();
                        return hydraService
                                .directlyAcceptConsentRequest(challenge, consentAccept)
                                .map(redirectTo -> "redirect:"+redirectTo);
                    }

                    model.addAttribute("challenge", challenge);
                    model.addAttribute("requestedScope", consentRequest.getRequestedScope());
                    model.addAttribute("subject", consentRequest.getSubject());
                    model.addAttribute("client", consentRequest.getClient());
                    return Mono.just("hydra/consent");
                });
    }

    @PostMapping("/consent")
    public Mono<String> consent(@RequestParam String challenge,
                                @RequestParam String[] grantScope,
                                @RequestParam(required = false, defaultValue = "0") Boolean remember,
                                @RequestParam String submit) {
        if (submit.equals("Deny")) {
            var consentReject = ConsentRejectDto.builder()
                    .error("access_denied")
                    .errorDescription("The resource owner denied the request")
                    .build();
            return hydraService
                    .rejectConsentRequest(challenge, consentReject)
                    .map(redirectTo -> "redirect:"+redirectTo);
        }

        return hydraService
                .getConsentRequest(challenge)
                .flatMap(consentRequest -> {
                    var consentAccept = ConsentAcceptDto.builder()
                            .grantScope(Arrays.asList(grantScope))
                            .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                            .remember(remember)
                            .rememberFor(86400)
                            .build();
                    return hydraService
                            .acceptConsentRequest(challenge, consentAccept)
                            .map(redirectTo -> "redirect:"+redirectTo);
                });
    }

    @GetMapping("/logout")
    public Mono<String> logout(@RequestParam(name = "logout_challenge") String challenge,
                              Model model) {
        return hydraService
                .getLogoutRequest(challenge)
                .map(e -> {
                    model.addAttribute("challenge", challenge);
                    return "hydra/logout";
                });
    }

    @PostMapping("/logout")
    public Mono<String> logout(@RequestParam String challenge,
                               @RequestParam String submit) {
        if (submit.equals("No")) {
            return hydraService
                    .rejectLogoutRequest(challenge)
                    .map(e -> "redirect:/");
        }

        return hydraService
                .acceptLogoutRequest(challenge)
                .map(redirectTo -> "redirect:"+redirectTo);
    }
}
