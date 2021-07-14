package net.jaggerwang.scip.gateway.adapter.api.security;

import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.common.usecase.exception.ApiException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.gateway.usercase.port.service.ReactiveAuthService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jagger Wang
 */
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    private ReactiveAuthService reactiveAuthService;

    public ReactiveUserDetailsServiceImpl(ReactiveAuthService reactiveAuthService) {
        this.reactiveAuthService = reactiveAuthService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<UserDTO> mono;
        try {
            if (username.matches("[0-9]+")) {
                mono = reactiveAuthService.infoByMobile(username);
            } else if (username.matches("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")) {
                mono = reactiveAuthService.infoByEmail(username);
            } else {
                mono = reactiveAuthService.infoByUsername(username);
            }
        } catch (ApiException e) {
            if (e.getCode() == ApiResult.Code.NOT_FOUND) {
                throw new UsernameNotFoundException("用户未找到");
            } else {
                throw e;
            }
        }

        return mono.flatMap(userDto -> reactiveAuthService.rolesOfUser(userDto.getId())
                .map(roles -> {
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(v -> new SimpleGrantedAuthority("ROLE_" + v.getName()))
                            .collect(Collectors.toList());
                    return new LoggedUser(userDto.getId(), userDto.getUsername(),
                            userDto.getPassword(), authorities);
                }));
    }
}