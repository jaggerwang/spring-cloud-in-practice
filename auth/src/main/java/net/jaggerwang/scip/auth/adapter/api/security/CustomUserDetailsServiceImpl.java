package net.jaggerwang.scip.auth.adapter.api.security;

import java.util.List;
import java.util.stream.Collectors;

import net.jaggerwang.scip.auth.usecase.AuthUsecase;
import net.jaggerwang.scip.common.adapter.api.security.LoggedUser;
import net.jaggerwang.scip.common.usecase.port.service.UserService;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jagger Wang
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private AuthUsecase authUsecase;
    private UserService userService;

    public CustomUserDetailsServiceImpl(AuthUsecase authUsecase, UserService userService) {
        this.authUsecase = authUsecase;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO;
        if (username.matches("[0-9]+")) {
            userDTO = userService.infoByMobile(username);
        } else if (username.matches("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+")) {
            userDTO = userService.infoByEmail(username);
        } else {
            userDTO = userService.infoByUsername(username);
        }
        if (userDTO == null) {
            throw new UsernameNotFoundException("用户未找到");
        }

        List<GrantedAuthority> authorities = authUsecase.rolesOfUser(userDTO.getId()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return new LoggedUser(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(),
                authorities);
    }
}