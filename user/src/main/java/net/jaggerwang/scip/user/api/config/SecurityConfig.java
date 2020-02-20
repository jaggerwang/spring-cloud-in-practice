package net.jaggerwang.scip.user.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void responseJson(HttpServletResponse response, HttpStatus status, RootDto data) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                responseJson(response, HttpStatus.UNAUTHORIZED,
                                        new RootDto("unauthenticated", "未认证")))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                responseJson(response, HttpStatus.FORBIDDEN,
                                        new RootDto("unauthorized", "未授权")))
                )
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/favicon.ico", "/actuator/**", "/user/register",
                                "/user/verifyPassword", "/user/logged", "/user/following")
                        .permitAll()
                        .anyRequest()
                        .hasAuthority("SCOPE_user"));
    }
}
