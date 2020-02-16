package net.jaggerwang.scip.gateway.adapter.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.async.FileService;
import net.jaggerwang.scip.common.usecase.port.service.async.PostService;
import net.jaggerwang.scip.common.usecase.port.service.async.StatService;
import net.jaggerwang.scip.common.usecase.port.service.async.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


abstract public class AbstractDataFetchers {
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

    public Map<String, DataFetcher> toMap() {
        var dataFetchers = new HashMap<String, DataFetcher>();
        var methods = this.getClass().getDeclaredMethods();
        for (var method: methods) {
            if (Modifier.isPublic(method.getModifiers()) &&
                    method.getReturnType().equals(DataFetcher.class)) {
                try {
                    dataFetchers.put(method.getName(), (DataFetcher) method.invoke(this));
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataFetchers;
    }
}
