package net.jaggerwang.scip.gateway.adapter.api.security;

import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.common.usecase.exception.ApiException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.gateway.usecase.port.service.ReactiveAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        var reactiveAuthService = (ReactiveAuthService) applicationContext.getBean(
                "reactiveAuthService");
        Mono<ApiResult<UserDTO>> infoRequest;
        try {
            if (username.matches("[0-9]+")) {
                infoRequest = reactiveAuthService.infoByMobile(username);
            } else if (username.matches("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")) {
                infoRequest = reactiveAuthService.infoByEmail(username);
            } else {
                infoRequest = reactiveAuthService.infoByUsername(username);
            }
        } catch (ApiException e) {
            if (e.getCode() == ApiResult.Code.NOT_FOUND) {
                throw new UsernameNotFoundException("用户未找到");
            } else {
                throw e;
            }
        }

        return infoRequest
                .flatMap(infoResult ->  reactiveAuthService
                        .rolesOfUser(infoResult.getData().getId())
                        .map(rolesResult -> {
                            var userDTO = infoResult.getData();
                            var roleDTOs = rolesResult.getData();
                            List<GrantedAuthority> authorities = roleDTOs.stream()
                                    .map(v -> new SimpleGrantedAuthority("ROLE_" + v.getName()))
                                    .collect(Collectors.toList());
                            return new LoggedUser(userDTO.getId(), userDTO.getUsername(),
                                    userDTO.getPassword(), authorities);
                        }));
    }
}