package luckylau.spring.session.service;


import luckylau.spring.session.entity.UserPO;
import luckylau.spring.session.repository.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author luckylau
 * @Date 2019/7/11
 */
@Service
public class SpringSecurityUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPO findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserPO findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserPO> list() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }
}
