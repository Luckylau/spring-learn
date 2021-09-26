package luckylau.spring.session.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2021/9/26
 * 强制下线
 */
@Service
@Slf4j
public class DefaultSessionManagement {
    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    public void forceUserLogOut(String username) {
        List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(username, false);
        for (SessionInformation sessionInformation : sessionInformations) {
            sessionInformation.expireNow();
        }
    }
}
