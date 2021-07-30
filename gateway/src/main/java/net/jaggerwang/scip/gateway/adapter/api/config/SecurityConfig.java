package net.jaggerwang.scip.gateway.adapter.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jagger Wang
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.resourceId}")
    private String resourceId;

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
    ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    /**
     * Map authorities in OAuth2User or OidcUser to authorities of OAuth2AuthenticationToken, this
     * is triggered when client got an access token.
     */
    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return authorities -> {
            // Just for an example
            var mappedAuthorities = new HashSet<GrantedAuthority>();
            authorities.forEach(authoritie -> {
                mappedAuthorities.add(authoritie);
            });
            return mappedAuthorities;
        };
    }

    /**
     * Customize OAuth2User (or OidcUser if using OpendID Connect protocol), such as bind user from
     * OAuth2 provider to a client's inner user, and get authorities of this inner user.
     */
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        var delegate = new OidcUserService();
        return userRequest -> {
            var oidcUser = delegate.loadUser(userRequest);

            var accessToken = userRequest.getAccessToken();
            var mappedAuthorities = new HashSet<GrantedAuthority>();

            // TODO
            // 1) Bind user from OAuth2 provider to a client's inner user.
            // 2) Get authorities from this inner user.

            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(),
                    oidcUser.getUserInfo());

            return oidcUser;
        };
    }

    /**
     * Convert a JWT issued by an OAuth2 service to an Authentication, this is triggered when
     * resource server received an access token. here you can extract resource roles in JWT or get
     * roles from other service.
     */
    private Converter<Jwt, Mono<JwtAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            var authorities = new JwtGrantedAuthoritiesConverter().convert(jwt);
            var resourceAccess = (Map<String, Object>) jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                var resource = (Map<String, Object>) resourceAccess.get(resourceId);
                if (resource != null) {
                    var roles = (Collection<String>) resource.get("roles");
                    if (roles != null) {
                        authorities = Stream.concat(authorities.stream(), roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)))
                                .collect(Collectors.toList());
                    }
                }
            }
            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
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
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }
}
