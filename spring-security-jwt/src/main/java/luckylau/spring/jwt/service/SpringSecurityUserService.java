package luckylau.spring.jwt.service;


import luckylau.spring.jwt.entity.UserInfo;
import luckylau.spring.jwt.entity.UserPO;
import luckylau.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Service
public class SpringSecurityUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserInfo findByUsername(String username) {
        UserPO userPo = userRepository.findByUsername(username);
        return userPo != null ? userPo.toUserInfo() : null;
    }
}
