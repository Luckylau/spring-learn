package luckylau.spring.session.security;

import lombok.extern.slf4j.Slf4j;
import luckylau.spring.session.entity.UserPO;
import luckylau.spring.session.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author luckylau
 * @Date 2021/9/25
 */
@Component
@Slf4j
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();

        UserPO userPO = userService.findByUsername(username);
        if (userPO == null) {
            throw new BadCredentialsException("用户不存在");
        }

        if (!passwordEncoder.matches(rawPassword, userPO.getPassword())) {
            throw new BadCredentialsException("用户名或密码不正确");
        }

        DefaultAuthenticationToken result = new DefaultAuthenticationToken(username, rawPassword, listUserGrantedAuthorities(userPO), userPO);
        result.setDetails(authentication.getDetails());
        return result;
    }

    private Set<GrantedAuthority> listUserGrantedAuthorities(UserPO user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
