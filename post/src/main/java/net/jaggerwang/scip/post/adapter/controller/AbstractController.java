package net.jaggerwang.scip.post.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.jaggerwang.scip.post.usecase.PostUsecases;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PostUsecases postUsecases;

    protected Long loggedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {
            return null;
        }

        var jwt = (Jwt) auth.getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }
}
