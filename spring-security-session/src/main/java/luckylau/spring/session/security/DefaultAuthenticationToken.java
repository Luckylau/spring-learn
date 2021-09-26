package luckylau.spring.session.security;

import luckylau.spring.session.entity.UserPO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Author luckylau
 * @Date 2021/9/25
 */
public class DefaultAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final UserPO user;

    public DefaultAuthenticationToken(Object principal, Object credentials,
                                      Collection<? extends GrantedAuthority> authorities, UserPO user) {
        super(principal, credentials, authorities);
        this.user = user;
    }

    public UserPO getUser() {
        return user;
    }
}
