package net.jaggerwang.scip.gateway.adapter.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class LoggedUser extends User {
    private static final long serialVersionUID = 1L;

    private final Long id;

    public LoggedUser(Long id, String username, String password,
            Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
