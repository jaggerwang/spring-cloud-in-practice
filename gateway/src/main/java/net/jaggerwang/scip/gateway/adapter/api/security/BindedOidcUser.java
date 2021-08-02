package net.jaggerwang.scip.gateway.adapter.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

/**
 * @author Jagger Wang
 */
public class BindedOidcUser extends DefaultOidcUser {
    private final LoggedUser loggedUser;

    public BindedOidcUser(LoggedUser loggedUser, Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken, OidcUserInfo userInfo) {
        super(authorities, idToken, userInfo);

        this.loggedUser = loggedUser;
    }

    public LoggedUser getLoggedUser() {
        return loggedUser;
    }
}
