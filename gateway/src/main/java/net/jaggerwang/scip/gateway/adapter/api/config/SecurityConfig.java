package net.jaggerwang.scip.gateway.adapter.api.config;

import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserBindRequestDTO;
import net.jaggerwang.scip.gateway.adapter.api.security.BindedOidcUser;
import net.jaggerwang.scip.gateway.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.gateway.usecase.port.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jagger Wang
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

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
     * OAuth2 provider to a client's internal user, and get authorities of this internal user.
     */
    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> reactiveOAuth2UserService() {
        var delegate = new OidcReactiveOAuth2UserService();
        return userRequest -> delegate
                .loadUser(userRequest)
                .flatMap(oidcUser -> {
                    // Bind OAuth2 provider's user to client's internal user
                    var clientRegistration = userRequest.getClientRegistration();
                    var userInfo = oidcUser.getUserInfo();
                    var userDTO = UserDTO.builder()
                            .username(clientRegistration.getRegistrationId() + "_" +
                                    userInfo.getPreferredUsername())
                            .password("")
                            .email(userInfo.getEmail())
                            .build();
                    return userService.bind(UserBindRequestDTO.builder()
                            .externalAuthProvider(clientRegistration.getRegistrationId())
                            .externalUserId(userInfo.getSubject())
                            .internalUser(userDTO)
                            .build())
                            .map(apiResult -> {
                                var bindedUserDTO = apiResult.getData();

                                // Extract client roles in access token
                                var authorities = oidcUser.getAuthorities();
                                var jwtDecoder = NimbusJwtDecoder.withJwkSetUri(
                                        clientRegistration.getProviderDetails().getJwkSetUri())
                                        .build();
                                var jwt = jwtDecoder.decode(userRequest.getAccessToken()
                                        .getTokenValue());
                                var resourceAccess = jwt.getClaimAsMap("resource_access");
                                if (resourceAccess != null) {
                                    var resource = (Map<String, Object>) resourceAccess.get(
                                            clientRegistration.getClientId());
                                    if (resource != null) {
                                        var roles = (Collection<String>) resource.get("roles");
                                        if (roles != null) {
                                            authorities = Stream.concat(authorities.stream(),
                                                    roles.stream()
                                                            .map(role -> new SimpleGrantedAuthority(
                                                                    "ROLE_" + role)))
                                                    .collect(Collectors.toList());
                                        }
                                    }
                                }

                                var loggedUser = new LoggedUser(bindedUserDTO.getId(),
                                        bindedUserDTO.getUsername(), bindedUserDTO.getPassword(),
                                        authorities);
                                return new BindedOidcUser(loggedUser, authorities,
                                        oidcUser.getIdToken(), oidcUser.getUserInfo());
                            });
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
