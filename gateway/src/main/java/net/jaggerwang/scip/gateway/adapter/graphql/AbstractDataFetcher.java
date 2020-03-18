package net.jaggerwang.scip.gateway.adapter.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.async.FileAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.PostAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.StatAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.async.UserAsyncService;
import net.jaggerwang.scip.gateway.api.security.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;


abstract public class AbstractDataFetcher {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private ReactiveAuthenticationManager authManager;

    @Autowired
    protected UserAsyncService userAsyncService;

    @Autowired
    protected PostAsyncService postAsyncService;

    @Autowired
    protected FileAsyncService fileAsyncService;

    @Autowired
    protected StatAsyncService statAsyncService;

    protected Mono<ServerWebExchange> getServerWebExchange() {
        return Mono.subscriberContext()
                .map(ctx -> ctx.get(ServerWebExchange.class));
    }

    protected Mono<WebSession> getWebSession() {
        return getServerWebExchange()
                .flatMap(exchange -> exchange.getSession());
    }

    protected Mono<SecurityContext> getSecurityContext() {
        return getWebSession()
                .flatMap(session -> ReactiveSecurityContextHolder.getContext()
                        .switchIfEmpty(Mono.fromSupplier(() -> {
                            var context = new SecurityContextImpl();
                            session.getAttributes().put(
                                    DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, context);
                            return context;
                        })));
    }

    protected Mono<LoggedUser> loginUser(String username, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(auth -> getSecurityContext()
                        .flatMap(context -> getWebSession()
                                .map(session -> {
                                    context.setAuthentication(auth);
                                    session.getAttributes().put(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, context);
                                    return (LoggedUser) auth.getPrincipal();
                                })));
    }

    protected Mono<LoggedUser> logoutUser() {
        return loggedUser()
                .flatMap(loggedUser -> getSecurityContext()
                        .flatMap(context -> getWebSession()
                                .map(session -> {
                                    context.setAuthentication(null);
                                    session.getAttributes().put(
                                            DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, context);
                                    return loggedUser;
                                })));
    }

    protected Mono<LoggedUser> loggedUser() {
        return getSecurityContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();
                    if (auth == null || auth instanceof AnonymousAuthenticationToken ||
                            !auth.isAuthenticated()) {
                        return Mono.empty();
                    }
                    return Mono.just((LoggedUser) auth.getPrincipal());
                });
    }

    protected Mono<Long> loggedUserId() {
        return loggedUser()
                .map(loggedUser -> loggedUser.getId());
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

    protected <T> CompletableFuture<T> monoWithContext(Mono<T> mono, DataFetchingEnvironment env) {
        return mono
                .subscriberContext(ctx -> env.getContext())
                .toFuture();
    }
}
