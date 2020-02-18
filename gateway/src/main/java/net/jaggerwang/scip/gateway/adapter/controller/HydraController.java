package net.jaggerwang.scip.gateway.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import net.jaggerwang.scip.common.usecase.port.service.async.HydraAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
@RequestMapping("/hydra")
public class HydraController {
    protected ObjectMapper objectMapper;
    protected UserAsyncService userAsyncService;
    protected HydraAsyncService hydraAsyncService;

    public HydraController(ObjectMapper objectMapper, UserAsyncService userAsyncService,
                           HydraAsyncService hydraAsyncService) {
        this.objectMapper = objectMapper;
        this.userAsyncService = userAsyncService;
        this.hydraAsyncService = hydraAsyncService;
    }

    @GetMapping("/login")
    public Mono<String> login(@RequestParam(name = "login_challenge") String challenge,
                              Model model) {
        return hydraAsyncService
                .getLoginRequest(challenge)
                .flatMap(loginRequest -> {
                    if (loginRequest.getSkip()) {
                        var loginAccept = LoginAcceptDto.builder()
                                .subject(loginRequest.getSubject())
                                .build();
                        return hydraAsyncService
                                .directlyAcceptLoginRequest(challenge, loginAccept)
                                .map(redirectTo -> "redirect:"+redirectTo);
                    }

                    model.addAttribute("challenge", challenge);
                    return Mono.just("hydra/login");
        });
    }

    @Data
    static class LoginForm {
        private String challenge;
        private String username;
        private String mobile;
        private String email;
        private String password;
        private Boolean remember = false;
        private String submit;
    }

    @PostMapping("/login")
    public Mono<String> login(@ModelAttribute LoginForm form, Model model) {
        if (form.submit.equals("No")) {
            var loginReject = LoginRejectDto.builder()
                    .error("login_rejected")
                    .errorDescription("The resource owner rejected to log in")
                    .build();
            return hydraAsyncService
                    .rejectLoginRequest(form.challenge, loginReject)
                    .map(redirectTo -> "redirect:" + redirectTo);
        }

        Mono<UserDto> mono;
        if (form.username != null) {
            mono = userAsyncService.verifyPasswordByUsername(form.username, form.password);
        } else if (form.mobile != null) {
            mono = userAsyncService.verifyPasswordByMobile(form.mobile, form.password);
        } else if (form.email != null) {
            mono = userAsyncService.verifyPasswordByEmail(form.email, form.password);
        } else {
            model.addAttribute("error", "用户名、手机或邮箱不能都为空");
            return Mono.just("hydra/login");
        }

        return mono
                .flatMap(userDto -> {
                    var loginAccept = LoginAcceptDto.builder()
                            .subject(userDto.getId().toString())
                            .remember(form.remember)
                            .rememberFor(86400)
                            .build();
                    return hydraAsyncService
                            .acceptLoginRequest(form.challenge, loginAccept)
                            .map(redirectTo -> "redirect:"+redirectTo);
                });
    }

    @GetMapping("/consent")
    public Mono<String> consent(@RequestParam(name = "consent_challenge") String challenge,
                                Model model) {
        return hydraAsyncService
                .getConsentRequest(challenge)
                .flatMap(consentRequest -> {
                    if (consentRequest.getSkip()) {
                        var consentAccept = ConsentAcceptDto.builder()
                                .grantScope(consentRequest.getRequestedScope())
                                .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                                .build();
                        return hydraAsyncService
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

    @Data
    static class ConsentForm {
        private String challenge;
        private String[] grantScope;
        private Boolean remember = false;
        private String submit;
    }

    @PostMapping("/consent")
    public Mono<String> consent(@ModelAttribute ConsentForm form) {
        if (form.submit.equals("Deny")) {
            var consentReject = ConsentRejectDto.builder()
                    .error("access_denied")
                    .errorDescription("The resource owner denied the request")
                    .build();
            return hydraAsyncService
                    .rejectConsentRequest(form.challenge, consentReject)
                    .map(redirectTo -> "redirect:"+redirectTo);
        }

        return hydraAsyncService
                .getConsentRequest(form.challenge)
                .flatMap(consentRequest -> {
                    var consentAccept = ConsentAcceptDto.builder()
                            .grantScope(Arrays.asList(form.grantScope))
                            .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                            .remember(form.remember)
                            .rememberFor(86400)
                            .build();
                    return hydraAsyncService
                            .acceptConsentRequest(form.challenge, consentAccept)
                            .map(redirectTo -> "redirect:"+redirectTo);
                });
    }

    @GetMapping("/logout")
    public Mono<String> logout(@RequestParam(name = "logout_challenge") String challenge,
                              Model model) {
        return hydraAsyncService
                .getLogoutRequest(challenge)
                .map(e -> {
                    model.addAttribute("challenge", challenge);
                    return "hydra/logout";
                });
    }

    @Data
    static class LogoutForm {
        private String challenge;
        private String submit;
    }

    @PostMapping("/logout")
    public Mono<String> logout(@ModelAttribute LogoutForm form) {
        if (form.submit.equals("No")) {
            return hydraAsyncService
                    .rejectLogoutRequest(form.challenge)
                    .map(e -> "redirect:/");
        }

        return hydraAsyncService
                .acceptLogoutRequest(form.challenge)
                .map(redirectTo -> "redirect:"+redirectTo);
    }
}
