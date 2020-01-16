package net.jaggerwang.scip.gateway.adapter.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.gateway.usecase.port.service.FileService;
import net.jaggerwang.scip.gateway.usecase.port.service.PostService;
import net.jaggerwang.scip.gateway.usecase.port.service.StatService;
import net.jaggerwang.scip.gateway.usecase.port.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


abstract public class AbstractResolver {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PostService postService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected StatService statService;

    protected Long loggedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {
            return null;
        }

        var jwt = (Jwt) auth.getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }
}
