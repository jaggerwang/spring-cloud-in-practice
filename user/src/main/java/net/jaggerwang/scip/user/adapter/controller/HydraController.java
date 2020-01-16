package net.jaggerwang.scip.user.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.auth.*;
import net.jaggerwang.scip.common.usecase.port.service.sync.HydraService;
import net.jaggerwang.scip.user.entity.UserEntity;
import net.jaggerwang.scip.user.usecase.UserUsecases;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

// TODO: can't permit access through gateway right now, so let hydra access user service directly,
//  see more on https://github.com/spring-cloud/spring-cloud-gateway/issues/1523
@Controller
@RequestMapping("/hydra")
public class HydraController {
    protected HydraService hydraService;
    protected UserUsecases userUsecases;

    public HydraController(HydraService hydraService, UserUsecases userUsecases) {
        this.hydraService = hydraService;
        this.userUsecases = userUsecases;
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "login_challenge") String challenge, Model model) {
        var loginRequest = hydraService.getLoginRequest(challenge);

        if (loginRequest.getSkip()) {
            var loginAccept = LoginAcceptDto.builder()
                    .subject(loginRequest.getSubject())
                    .build();
            var redirectTo = hydraService.directlyAcceptLoginRequest(challenge, loginAccept);
            return "redirect:" + redirectTo;
        }

        model.addAttribute("challenge", challenge);
        return "hydra/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String challenge,
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
            var redirectTo = hydraService.rejectLoginRequest(challenge, loginReject);
            return "redirect:" + redirectTo;
        }

        Optional<UserEntity> userEntity;
        if (username != null) {
            userEntity = userUsecases.infoByUsername(username);
        } else if (mobile != null) {
            userEntity = userUsecases.infoByMobile(mobile);
        } else if (email != null) {
            userEntity = userUsecases.infoByEmail(email);
        } else {
            model.addAttribute("error", "用户名、手机或邮箱不能都为空");
            return "hydra/login";
        }

        if (userEntity.isEmpty() ||
                !userUsecases.matchPassword(password, userEntity.get().getPassword())) {
            model.addAttribute("error", "用户名或密码错误");
            return "hydra/login";
        }

        var loginAccept = LoginAcceptDto.builder()
                .subject(userEntity.get().getId().toString())
                .remember(remember)
                .rememberFor(86400)
                .build();
        var redirectTo = hydraService.acceptLoginRequest(challenge, loginAccept);
        return "redirect:" + redirectTo;
    }

    @GetMapping("/consent")
    public String consent(@RequestParam(name = "consent_challenge") String challenge, Model model) {
        var consentRequest = hydraService.getConsentRequest(challenge);
        if (consentRequest.getSkip()) {
            var consentAccept = ConsentAcceptDto.builder()
                    .grantScope(consentRequest.getRequestedScope())
                    .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                    .build();
            var redirectTo = hydraService
                    .directlyAcceptConsentRequest(challenge, consentAccept);
            return "redirect:" + redirectTo;
        }

        model.addAttribute("challenge", challenge);
        model.addAttribute("requestedScope", consentRequest.getRequestedScope());
        model.addAttribute("subject", consentRequest.getSubject());
        model.addAttribute("client", consentRequest.getClient());
        return "hydra/consent";
    }

    @PostMapping("/consent")
    public String consent(@RequestParam String challenge,
                                @RequestParam String[] grantScope,
                                @RequestParam(required = false, defaultValue = "0") Boolean remember,
                                @RequestParam String submit) {
        if (submit.equals("Deny")) {
            var consentReject = ConsentRejectDto.builder()
                    .error("access_denied")
                    .errorDescription("The resource owner denied the request")
                    .build();
            var redirectTo = hydraService.rejectConsentRequest(challenge, consentReject);
            return "redirect:" + redirectTo;
        }

        var consentRequest = hydraService.getConsentRequest(challenge);
        var consentAccept = ConsentAcceptDto.builder()
                .grantScope(Arrays.asList(grantScope))
                .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                .remember(remember)
                .rememberFor(86400)
                .build();
        var redirectTo = hydraService.acceptConsentRequest(challenge, consentAccept);
        return "redirect:" + redirectTo;
    }

    @GetMapping("/logout")
    public String logout(@RequestParam(name = "logout_challenge") String challenge,
                              Model model) {
        hydraService.getLogoutRequest(challenge);

        model.addAttribute("challenge", challenge);
        return "hydra/logout";
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String challenge,
                               @RequestParam String submit) {
        if (submit.equals("No")) {
            hydraService.rejectLogoutRequest(challenge);

            return "redirect:/";
        }

        var redirectTo = hydraService.acceptLogoutRequest(challenge);
        return "redirect:" + redirectTo;
    }
}
