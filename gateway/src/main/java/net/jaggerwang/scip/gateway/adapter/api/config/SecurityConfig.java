package net.jaggerwang.scip.gateway.adapter.api.config;

import net.jaggerwang.scip.gateway.adapter.api.security.BindedOidcUser;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Jagger Wang
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService userDetailsService) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
                userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    /**
     * Store access token in session.
     */
    @Bean
    ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    /**
     * Customize OAuth2User (or OidcUser if using OpendID Connect protocol), such as bind user from
     * OAuth2 provider to a client's inner user, and get authorities of this inner user.
     */
    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> reactiveOAuth2UserService() {
        var delegate = new OidcReactiveOAuth2UserService();
        return userRequest -> delegate
                .loadUser(userRequest)
                .map(oidcUser -> {
                    // TODO
                    // 1) Bind user from OAuth2 provider to a client's inner user.
                    // 2) Get authorities of this inner user.
                    var authorities = oidcUser.getAuthorities();
                    var loggedUser = new LoggedUser(0L, oidcUser.getName(), "", authorities);

                    return new BindedOidcUser(loggedUser, authorities, oidcUser.getIdToken(),
                            oidcUser.getUserInfo());
                });
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/", "/actuator/**", "/login", "/logout", "/auth/**",
                                "/user/user/register", "/file/files/**").permitAll()
                        .pathMatchers("/user/**").hasRole("user")
                        .pathMatchers("/post/**").hasRole("post")
                        .pathMatchers("/file/**").hasRole("file")
                        .pathMatchers("/stat/**").hasRole("stat")
                        .anyExchange().authenticated())
                .oauth2Login(oAuth2LoginSpec -> {})
                .build();
    }
}
