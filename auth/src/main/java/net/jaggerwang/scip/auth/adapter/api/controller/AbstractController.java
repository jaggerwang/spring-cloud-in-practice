package net.jaggerwang.scip.auth.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.auth.usecase.AuthUsecase;
import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.common.usecase.port.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jagger Wang
 */
abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected AuthUsecase authUsecase;

    @Autowired
    protected UserService userService;

    protected LoggedUser loginUser(String username, String password) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        var sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        return (LoggedUser) auth.getPrincipal();
    }

    protected LoggedUser logoutUser() {
        var loggedUser = loggedUser();
        SecurityContextHolder.getContext().setAuthentication(null);
        return loggedUser;
    }

    protected LoggedUser loggedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                !auth.isAuthenticated()) {
            return null;
        }
        return (LoggedUser) auth.getPrincipal();
    }

    protected Long loggedUserId() {
        var loggedUser = loggedUser();
        if (loggedUser == null) {
            return  null;
        }
        return loggedUser.getId();
    }
}
