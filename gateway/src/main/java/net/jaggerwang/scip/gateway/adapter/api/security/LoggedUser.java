package net.jaggerwang.scip.gateway.adapter.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * @author Jagger Wang
 */
public class LoggedUser extends User {
    private final Long id;

    public LoggedUser(Long id, String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
