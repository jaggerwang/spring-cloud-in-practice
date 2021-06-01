package net.jaggerwang.scip.gateway.adapter.api.security;

import net.jaggerwang.scip.common.usecase.exception.InternalApiException;
import net.jaggerwang.scip.common.usecase.port.service.UserAsyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
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

@Service
@Primary
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private UserAsyncService userAsyncService;

    public CustomUserDetailsService(UserAsyncService userAsyncService) {
        this.userAsyncService = userAsyncService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<UserDTO> mono;
        try {
            if (username.matches("[0-9]+")) {
                mono = userAsyncService.infoByMobile(username, true);
            } else if (username.matches("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")) {
                mono = userAsyncService.infoByEmail(username, true);
            } else {
                mono = userAsyncService.infoByUsername(username, true);
            }
        } catch (InternalApiException e) {
            if ("not_found".equals(e.getCode())) {
                throw new UsernameNotFoundException("用户未找到");
            } else {
                throw e;
            }
        }

        return mono.flatMap(userDto -> userAsyncService.roles(userDto.getUsername())
                .map(roles -> {
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(v -> new SimpleGrantedAuthority("ROLE_" + v.getName()))
                            .collect(Collectors.toList());
                    return new LoggedUser(userDto.getId(), userDto.getUsername(),
                            userDto.getPassword(), authorities);
                }));
    }
}
